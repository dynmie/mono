package me.dynmie.mono.client.player;

import lombok.Getter;
import lombok.Setter;
import me.dynmie.mono.client.timer.PlaybackTimer;
import me.dynmie.mono.client.utils.ConsoleUtils;
import me.dynmie.mono.client.utils.FrameUtils;
import org.bytedeco.ffmpeg.global.swscale;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author dynmie
 */
public class VideoPlayer {

    private final OutputStream outputStream;
    private final File source;
    private volatile @Getter int width;
    private volatile @Getter int height;
    private volatile @Getter @Setter boolean color;

    private volatile @Getter boolean running = false;
    private volatile @Getter boolean paused = false;

    private @Setter Runnable onNaturalEnd = null;

    private Thread thread;

    public VideoPlayer(OutputStream outputStream, File source, int width, int height, boolean color) {
        this.outputStream = outputStream;
        this.source = source;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void setResolution(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void play() {
        paused = false;
        synchronized (this) {
            notifyAll();
        }
    }

    public void pause() {
        paused = true;
    }

    public void stop() {
        running = false;
        synchronized (this) {
            notifyAll();
        }
    }

    private void createThread() {
        if (running) {
            throw new IllegalStateException("This getVideos player is already running!");
        }
        running = true;
        thread = Thread.startVirtualThread(() -> {
            try (
                    FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(source);
                    Java2DFrameConverter converter = new Java2DFrameConverter()
            ) {
                grabber.setImageScalingFlags(swscale.SWS_BICUBIC);
                grabber.setImageWidth(width);
                grabber.setImageHeight(height);

                grabber.start();

                PlaybackTimer playbackTimer;
                SourceDataLine audioLine;
                if (grabber.getAudioChannels() > 0) {
                    AudioFormat audioFormat = new AudioFormat(
                            grabber.getSampleRate(),
                            16,
                            grabber.getAudioChannels(),
                            true,
                            true
                    );
                    DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
                    audioLine = (SourceDataLine) AudioSystem.getLine(info);
                    audioLine.open(audioFormat);
                    audioLine.start();

                    playbackTimer = PlaybackTimer.create(audioLine);
                } else {
                    audioLine = null;
                    playbackTimer = PlaybackTimer.create();
                }

                ExecutorService imageExecutor = Executors.newSingleThreadExecutor();
                ExecutorService audioExecutor = Executors.newSingleThreadExecutor();

                long maxReadAheadBufferMicros = TimeUnit.MILLISECONDS.toMicros(1000);

                PrintWriter writer = new PrintWriter(outputStream, false);

                long lastTimestamp = -1L;

                while (!Thread.interrupted() && running) {
                    synchronized (this) {
                        while (paused) {
                            try {
                                wait();
                                if (!running) return;
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                return;
                            }
                        }
                    }

                    if (width != grabber.getImageWidth() || height != grabber.getImageHeight()) {
                        grabber.setImageWidth(width);
                        grabber.setImageHeight(height);
                    }

                    Frame frame = grabber.grab();
                    if (frame == null) break;

                    if (lastTimestamp == -1L) {
                        playbackTimer.init();
                    }

                    lastTimestamp = frame.timestamp;

                    // if frame is a getVideos frame
                    if (frame.image != null) {
                        Frame imageFrame = frame.clone();

                        final boolean col = color;
                        imageExecutor.submit(() -> {
                            if (width != imageFrame.imageWidth || height != imageFrame.imageHeight) {
                                return;
                            }

                            // sync getVideos with audio
                            long delayMicros = imageFrame.timestamp - playbackTimer.elapsedMicros();

                            // if getVideos is faster than audio
                            if (delayMicros > 0) {
                                // wait for audio to catch up with the getVideos
                                try {
                                    Thread.sleep(TimeUnit.MICROSECONDS.toMillis(delayMicros));
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            if (delayMicros < 0) return; // we're behind! skip the frame.

                            BufferedImage image = converter.convert(imageFrame);
                            imageFrame.close();
                            String text = FrameUtils.convertFrameToText(image, col);

                            ConsoleUtils.resetCursorPosition();
                            writer.append(text);
                            writer.flush();
                        });
                    } else if (frame.samples != null) { // if frame is audio frame
                        if (audioLine == null) {
                            throw new IllegalStateException("audio line was not initialized!");
                        }

                        ShortBuffer sampleShortBuffer = (ShortBuffer) frame.samples[0];
                        sampleShortBuffer.rewind();

                        ByteBuffer outBuffer = ByteBuffer.allocate(sampleShortBuffer.capacity() * 2);

                        for (int i = 0; i < sampleShortBuffer.capacity(); i++) {
                            short val = sampleShortBuffer.get(i);
                            outBuffer.putShort(val);
                        }

                        audioExecutor.submit(() -> {
                            audioLine.write(outBuffer.array(), 0, outBuffer.capacity());
                            outBuffer.clear();
                        });
                    }


                    // ensure that the audio doesn't go faster than the getVideos
                    long timeStampDeltaMicros = frame.timestamp - playbackTimer.elapsedMicros();
                    if (timeStampDeltaMicros > maxReadAheadBufferMicros) {
                        Thread.sleep(TimeUnit.MICROSECONDS.toMillis(timeStampDeltaMicros - maxReadAheadBufferMicros));
                    }
                }

                grabber.stop();
                grabber.release();

                if (audioLine != null) {
                    audioLine.stop();
                }

                audioExecutor.shutdownNow();
                audioExecutor.awaitTermination(10, TimeUnit.SECONDS);
                audioExecutor.close();

                imageExecutor.shutdownNow();
                imageExecutor.awaitTermination(10, TimeUnit.SECONDS);
                imageExecutor.close();
            } catch (FrameGrabber.Exception | LineUnavailableException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            running = false;
            paused = false;

            if (onNaturalEnd != null) {
                onNaturalEnd.run();
            }
        });
    }

    public void start() {
        if (running) {
            throw new IllegalStateException("This getVideos player is already running!");
        }
        createThread();
    }

    public void awaitFinish() throws InterruptedException {
        if (thread == null || !running) {
            throw new IllegalStateException("The player isn't running!");
        }
        thread.join();
    }

}

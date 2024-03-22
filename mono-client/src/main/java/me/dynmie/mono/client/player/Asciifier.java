package me.dynmie.mono.client.player;

import lombok.AllArgsConstructor;
import me.dynmie.mono.client.utils.ConsoleUtils;
import me.dynmie.mono.client.utils.RGBUtils;

import java.awt.image.BufferedImage;
import java.util.stream.IntStream;

/**
 * @author dynmie
 */
@AllArgsConstructor
public class Asciifier {
    public static final float DITHER_FACTOR = 0.0625f;
    public static final int DITHER_NEIGHBOR_RIGHT_FACTOR = 7;
    public static final int DITHER_NEIGHBOR_BOTTOM_LEFT_FACTOR = 3;
    public static final int DITHER_NEIGHBOR_BOTTOM_FACTOR = 5;
    public static final int DITHER_NEIGHBOR_BOTTOM_RIGHT_FACTOR = 1;

    public static final char[] DEFAULT_BRIGHTNESS_LEVELS = " .-+*wGHM#&%".toCharArray();

    private final boolean color;
    private final boolean fullPixel;
    private final boolean textDithering;
    private final char[] brightnessLevels;

    public String createFrame(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        float[][] textDitheringErrors = new float[width][height];

        String[] lines = new String[height];
        IntStream.range(0, height)
//                .parallel() // we have cores, use them!
                .forEach(y -> {
                    StringBuilder builder = new StringBuilder();
                    int prevColor = -1;

                    for (int x = 0; x < width; x++) {
                        int currentColor = image.getRGB(x, y);

                        int red   = (currentColor >>> 16) & 0xFF;
                        int green = (currentColor >>>  8) & 0xFF;
                        int blue  = (currentColor       ) & 0xFF;

                        float brightness = RGBUtils.getBrightness(red, green, blue); // percentage

                        if (textDithering && !fullPixel) {
                            float thisError = textDitheringErrors[x][y];
                            brightness += thisError;

                            float perceivedBrightness = (float) indexFromBrightness(brightness) / (brightnessLevels.length - 1);
                            float error = (brightness - perceivedBrightness) * DITHER_FACTOR;

                            writeDitheringError(width, height, x, y, error, textDitheringErrors);

                            brightness = Math.clamp(brightness, 0, 1); // min 0, max 1
                        }

                        if (color) {
                            // some optimizations
                            if (x == 0 || currentColor != prevColor) {
                                char brightnessChar = getRGBBrightnessCharFromColor(brightness);

                                String ret;
                                if (brightnessChar == ' ' && !fullPixel) {
                                    ret =  " ";
                                } else if (fullPixel) {
                                    ret = ConsoleUtils.getBackgroundColorEscapeCode(red, green, blue) + brightnessChar;
                                } else {
                                    ret = ConsoleUtils.getForegroundColorEscapeCode(red, green, blue) + brightnessChar;
                                }

                                builder.append(ret);
                                prevColor = currentColor;
                            } else {
                                builder.append(getRGBBrightnessCharFromColor(brightness));
                            }
                        } else {
                            builder.append(getBrightnessCharFromColor(brightness));
                        }
                    }

                    lines[y] = builder.toString();
                });
        return String.join("\n", lines);
    }

    private int indexFromBrightness(float brightness) {
        return (int) (brightness * (brightnessLevels.length - 1));
    }

    private char getBrightnessCharFromColor(float brightness) {
        return brightnessLevels[indexFromBrightness(brightness)];
    }

    private char getRGBBrightnessCharFromColor(float brightness) {
        if (fullPixel) {
            return ' ';
        }
        return brightnessLevels[indexFromBrightness(brightness)];
    }

    private static void writeDitheringError(int width, int height, int x, int y, float error, float[][] errors) {
        if (x < width - 1) {
            errors[x + 1][y] += error * DITHER_NEIGHBOR_RIGHT_FACTOR;
        }

        if (y < height - 1) {
            if (x > 0) {
                errors[x - 1][y + 1] += error * DITHER_NEIGHBOR_BOTTOM_LEFT_FACTOR;
            }
            errors[x][y + 1] += error * DITHER_NEIGHBOR_BOTTOM_FACTOR;
            if (x < width - 1) {
                errors[x + 1][y + 1] += error * DITHER_NEIGHBOR_BOTTOM_RIGHT_FACTOR;
            }
        }
    }

    private static int clamp255(int value) {
        return Math.min(Math.max(value, 0), 255);
    }

}

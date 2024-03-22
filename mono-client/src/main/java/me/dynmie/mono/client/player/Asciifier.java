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
    public static final char[] DEFAULT_BRIGHTNESS_LEVELS = " .-+*wGHM#&%".toCharArray();

    private final boolean color;
    private final boolean fullPixel;
    private final char[] brightnessLevels;

    public String createFrame(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        String[] lines = new String[height];
        IntStream.range(0, height)
                .parallel() // we have cores, use them!
                .forEach(y -> {
                    StringBuilder builder = new StringBuilder();
                    int prevColor = -1;

                    for (int x = 0; x < width; x++) {
                        int currentColor = image.getRGB(x, y);

                        int red   = (currentColor >>> 16) & 0xFF;
                        int green = (currentColor >>>  8) & 0xFF;
                        int blue  = (currentColor       ) & 0xFF;

                        float brightness = RGBUtils.getBrightness(red, blue, green);

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
}

package me.dynmie.mono.client.utils;

import me.dynmie.mono.shared.player.PlayerConfig;

import java.awt.image.BufferedImage;
import java.util.stream.IntStream;

/**
 * @author dynmie
 */
public class FrameUtils {

    public static final char[] BRIGHTNESS_LEVELS = " .-+*wGHM#&%".toCharArray();
    public static final char TRUE_COLOR_CHAR = ' ';

    public static String convertFrameToText(BufferedImage image, PlayerConfig config) {
        boolean color = config.isColor();

        int width = image.getWidth();
        int height = image.getHeight();

        String[] lines = new String[height];
        IntStream.range(0, height)
                .parallel()
                .forEach(y -> {
                    StringBuilder builder = new StringBuilder();
                    int prevColor = -1;

                    for (int x = 0; x < width; x++) {
                        int currentColor = image.getRGB(x, y);
                        // some optimizations
                        if (color && (x == 0 || currentColor != prevColor)) {
                            builder.append(getRGBColoredCharacter(currentColor, config.isTrueColor()));
                            prevColor = currentColor;
                        } else if (color) {
                            float brightness = RGBUtils.getBrightness(currentColor);
                            builder.append(getRGBBrightnessCharFromColor(brightness, config.isTrueColor()));
                        } else {
                            float brightness = RGBUtils.getBrightness(currentColor);
                            builder.append(getBrightnessCharFromColor(brightness));
                        }
                    }

                    lines[y] = builder.toString();
                });
        return String.join("\n", lines);
    }

    private static char getBrightnessCharFromColor(float brightness) {
        return BRIGHTNESS_LEVELS[(int) (brightness * (BRIGHTNESS_LEVELS.length - 1))];
    }

    private static char getRGBBrightnessCharFromColor(float brightness, boolean trueColor) {
        if (trueColor) {
            return TRUE_COLOR_CHAR;
        }
        return BRIGHTNESS_LEVELS[(int) (brightness * (BRIGHTNESS_LEVELS.length - 1))];
    }

    public static String getRGBColoredCharacter(int color, boolean trueColor) {
        int red   = (color >>> 16) & 0xFF;
        int green = (color >>>  8) & 0xFF;
        int blue  = (color       ) & 0xFF;

        // calc luminance in range 0.0 to 1.0; using SRGB luminance constants
        float brightness = (red * 0.2126f + green * 0.7152f + blue * 0.0722f) / 255;

        char brightnessChar = getRGBBrightnessCharFromColor(brightness, trueColor);
        if (brightnessChar == ' ' && !trueColor) return " ";
        if (trueColor) {
            return ConsoleUtils.getBackgroundColorEscapeCode(red, green, blue) + brightnessChar;
        }

        return ConsoleUtils.getForegroundColorEscapeCode(red, green, blue) + brightnessChar;
    }

}

package me.dynmie.mono.client.utils;

import me.dynmie.mono.shared.player.PlayerConfig;

import java.awt.image.BufferedImage;
import java.util.StringJoiner;

/**
 * @author dynmie
 */
public class FrameUtils {

    public static final char[] BRIGHTNESS_LEVELS = "          `.-':_,^=;><+!rc*/z?sLTv)J7(|F{C}fI31tlu[neoZ5Yxya]2ESwqkP6h9d4VpOGbUAKXHm8RD#$Bg0MNWQ%&@@@"
            .toCharArray();

    public static final char[] RGB_BRIGHTNESS_LEVELS = "     `.-':_,^=;><+!rc*/z?sLTv)J7(|F{C}fI31tlu[neoZ5Yxya]2ESwqkP6h9d4VpOGbUAKXHm8RD#$Bg0MNWQ%&@@@"
            .toCharArray();

    @SuppressWarnings("UnnecessaryUnicodeEscape") // apparently it doesn't work if you just copy and paste lmao
    public static final char TRUE_COLOR_CHAR = '\u2588';

    public static String convertFrameToText(BufferedImage image, PlayerConfig config) {
        boolean color = config.isColor();

        StringJoiner joiner = new StringJoiner("\n");
        for (int y = 0; y < image.getHeight(); y++) {
            StringBuilder builder = new StringBuilder();
            int prevColor = -1;

            for (int x = 0; x < image.getWidth(); x++) {
                int currentColor = image.getRGB(x, y);
                // some optimizations
                if (color && currentColor != prevColor) {
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
            joiner.add(builder);
        }

        return joiner.toString();
    }

    private static char getBrightnessCharFromColor(float brightness) {
        return BRIGHTNESS_LEVELS[(int) (brightness * (BRIGHTNESS_LEVELS.length - 1))];
    }

    private static char getRGBBrightnessCharFromColor(float brightness, boolean trueColor) {
        if (trueColor) {
            return TRUE_COLOR_CHAR;
        }
        return RGB_BRIGHTNESS_LEVELS[(int) (brightness * (RGB_BRIGHTNESS_LEVELS.length - 1))];
    }

    public static String getRGBColoredCharacter(int color, boolean trueColor) {
        int red   = (color >>> 16) & 0xFF;
        int green = (color >>>  8) & 0xFF;
        int blue  = (color       ) & 0xFF;

        // calc luminance in range 0.0 to 1.0; using SRGB luminance constants
        float brightness = (red * 0.2126f + green * 0.7152f + blue * 0.0722f) / 255;

        char brightnessChar = getRGBBrightnessCharFromColor(brightness, trueColor);
        if (brightnessChar == ' ') return " ";

        return getColorEscapeCode(red, green, blue) + brightnessChar;
    }

    public static String getColorEscapeCode(int red, int green, int blue) {
        return "\033[38;2;%s;%s;%sm".formatted(red, green, blue);
    }

}

package me.dynmie.mono.client.utils;

/**
 * @author dynmie
 */
public class ConsoleUtils {

    public static void setCursorVisibility(boolean visibility) {
        char val = visibility ? 'h' : 'l';
        System.out.printf("\033[?25%s", val);
    }

    public static void setCursorPosition(int row, int column) {
        System.out.printf("%c[%d;%df", 0x1B, row, column);
    }

    public static void resetCursorPosition() {
        setCursorPosition(0, 0);
    }

}

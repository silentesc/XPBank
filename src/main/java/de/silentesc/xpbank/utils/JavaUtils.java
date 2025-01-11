package de.silentesc.xpbank.utils;

public class JavaUtils {
    public static boolean isStringInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }
}

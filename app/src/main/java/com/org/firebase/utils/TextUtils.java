package com.org.firebase.utils;

public class TextUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isEmptyString(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.toString().trim().isEmpty();
    }

    public static String getString(String input) {
        return input == null || input.toString().trim().isEmpty() ? "" : input;
    }

    /**
     * this will prettify the input string.
     * If the string is null then it will return empty string else It will trim the string and return it.
     *
     * @param str string to be prettified for display.
     * @return If the string is null then it will return empty string else It will trim the string and return it.
     */
    public static String displayPretty(String str) {
        return str == null ? "" : str.trim();
    }
}

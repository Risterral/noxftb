package com.noxftb.www.votinghelperplugin.utils;

public class ParsingUtil {

    public static String parseString(String input) {
        if ("null".equals(input)) {
            return null;
        }
        return input;
    }

    public static Long parseLong(String input) {
        if ("null".equals(input)) {
            return null;
        }
        return Long.parseLong(input);
    }

    public static Integer parseInteger(String input) {
        if ("null".equals(input)) {
            return null;
        }
        return Integer.parseInt(input);
    }

    public static Double parseDouble(String input) {
        if ("null".equals(input)) {
            return null;
        }
        return Double.parseDouble(input);
    }

    public static Boolean parseBoolean(String input) {
        if ("null".equals(input)) {
            return null;
        }
        return "true".equals(input);
    }
}

package edu.org.utils;

public class StringUtil {
    public static Boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}

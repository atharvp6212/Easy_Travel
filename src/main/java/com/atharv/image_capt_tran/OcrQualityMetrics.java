package com.atharv.image_capt_tran;

import java.util.Locale;

/** Pure-Java metrics for reproducible OCR evaluation. */
public final class OcrQualityMetrics {
    private OcrQualityMetrics() {}

    public static String normalize(String value) {
        if (value == null) return "";
        return value.toLowerCase(Locale.ROOT)
                .replaceAll("[^\\p{L}\\p{N}\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    public static int levenshteinDistance(String expected, String actual) {
        String left = normalize(expected);
        String right = normalize(actual);
        int[] previous = new int[right.length() + 1];
        int[] current = new int[right.length() + 1];
        for (int j = 0; j <= right.length(); j++) previous[j] = j;
        for (int i = 1; i <= left.length(); i++) {
            current[0] = i;
            for (int j = 1; j <= right.length(); j++) {
                int replacement = previous[j - 1] + (left.charAt(i - 1) == right.charAt(j - 1) ? 0 : 1);
                current[j] = Math.min(Math.min(previous[j] + 1, current[j - 1] + 1), replacement);
            }
            int[] swap = previous;
            previous = current;
            current = swap;
        }
        return previous[right.length()];
    }

    public static double characterErrorRate(String expected, String actual) {
        String normalized = normalize(expected);
        if (normalized.isEmpty()) return normalize(actual).isEmpty() ? 0.0 : 1.0;
        return (double) levenshteinDistance(expected, actual) / normalized.length();
    }
}

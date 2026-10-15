package com.atharv.image_capt_tran;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OcrQualityMetricsTest {
    @Test
    public void normalizesCasePunctuationAndWhitespace() {
        assertEquals("hello world", OcrQualityMetrics.normalize(" Hello,   WORLD! "));
    }

    @Test
    public void calculatesLevenshteinDistance() {
        assertEquals(3, OcrQualityMetrics.levenshteinDistance("kitten", "sitting"));
    }

    @Test
    public void calculatesCharacterErrorRate() {
        assertEquals(0.2, OcrQualityMetrics.characterErrorRate("hello", "hallo"), 0.0001);
    }
}

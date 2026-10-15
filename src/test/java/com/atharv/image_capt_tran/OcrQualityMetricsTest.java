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

    @Test
    public void wordErrorRateMeasuresWordSubstitution() {
        assertEquals(1.0 / 3.0, OcrQualityMetrics.wordErrorRate("one two three", "one four three"), 0.0001);
    }

    @Test
    public void adaptivePolicyRespondsToQuality() {
        assertEquals(AdaptiveOcrPolicy.Pipeline.LOW_LIGHT, AdaptiveOcrPolicy.choose(40, 100, 0));
        assertEquals(AdaptiveOcrPolicy.Pipeline.DESKEW_AND_DENOISE, AdaptiveOcrPolicy.choose(120, 100, 5));
    }
}

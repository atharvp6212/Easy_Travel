package com.atharv.image_capt_tran;

/** Explainable preprocessing selection for controlled OCR experiments. */
public final class AdaptiveOcrPolicy {
    public enum Pipeline { ORIGINAL, DENOISE, LOW_LIGHT, DESKEW_AND_DENOISE }

    private AdaptiveOcrPolicy() {}

    public static Pipeline choose(double brightness, double blurScore, double skewDegrees) {
        if (brightness < 0 || brightness > 255 || blurScore < 0) {
            throw new IllegalArgumentException("Invalid image-quality measurements");
        }
        if (Math.abs(skewDegrees) >= 3) return Pipeline.DESKEW_AND_DENOISE;
        if (brightness < 70) return Pipeline.LOW_LIGHT;
        if (blurScore < 80) return Pipeline.DENOISE;
        return Pipeline.ORIGINAL;
    }
}

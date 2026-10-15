# Multilingual OCR and Assistive Translation Application

An Android application that captures or imports an image, extracts text with
Google ML Kit, translates the recognized text between supported languages and
reads the result aloud using text-to-speech.

## Technical scope

- Camera and gallery input with cropping
- On-device text recognition
- Language identification and translation
- Text-to-speech accessibility output
- Pure-Java OCR quality metrics for reproducible experiments
- Character Error Rate based on a memory-efficient Levenshtein algorithm

The Levenshtein implementation uses `O(n*m)` time and `O(m)` auxiliary space,
where `n` and `m` are the normalized reference and prediction lengths.

## Evaluation plan

Create a labelled image set covering blur, rotation, low light, perspective and
different text sizes. Compare OCR output using Character Error Rate, processing
latency and peak memory. This makes preprocessing decisions measurable rather
than relying on visual impressions.

## Build and test

```bash
./gradlew test
./gradlew assembleDebug
```

The current source layout is retained for compatibility and explicitly mapped in
the Gradle source sets. No API credentials should be committed to the repository.

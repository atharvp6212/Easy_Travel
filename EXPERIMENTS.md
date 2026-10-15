# OCR experiment protocol

Create a manifest with image ID, language, script, ground-truth transcription,
lighting, blur, rotation, device and resolution. Keep a fixed held-out test set.

Compare original, denoising, low-light and deskew-plus-denoising policies.
Report CER, WER, median and p95 latency, peak memory and model size. Stratify
results by language and degradation. Tune thresholds on validation data only.

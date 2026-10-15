# Architecture

The Android UI acquires an image and invokes on-device ML Kit components. Pure
Java classes contain metrics and adaptive decision logic, allowing experiments
without Android hardware. CER and WER use dynamic programming with `O(n*m)`
time and linear auxiliary space. API credentials are environment-injected.

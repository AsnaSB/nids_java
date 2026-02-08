package core.contracts;

import java.time.Instant;

/**
 * Output of a detection module for one FeatureVector.
 */
public interface DetectionResult {

    /** Classification label: e.g., "BENIGN", "DOS", "PROBE", "ANOMALY". */
    String label();

    /**
     * Confidence score in range [0.0, 1.0].
     * Rule-based detectors can use fixed values like 0.9.
     */
    double confidence();

    /** Timestamp tied to the analyzed traffic event. */
    Instant timestamp();

    /**
     * Human-readable explanation of why it was labeled that way.
     * Example: "SYN rate exceeded threshold (120/s > 60/s)".
     */
    String reason();

    /** The input feature vector that produced this result (optional but recommended). */
    FeatureVector input();
}

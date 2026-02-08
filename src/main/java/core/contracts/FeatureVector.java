package core.contracts;

import java.time.Instant;
import java.util.Map;

/**
 * A standardized set of features extracted from a TrafficRecord.
 * Detection modules should rely on this instead of raw TrafficRecord fields.
 */
public interface FeatureVector {

    /** Timestamp of the underlying traffic record. */
    Instant timestamp();

    /**
     * Link back to the original record (useful for debugging and explainability).
     * Can be null if you don't want to carry the source around.
     */
    TrafficRecord source();

    /**
     * Extracted features as name -> numeric value.
     * Examples: packet_rate, byte_rate, syn_count, dst_port, flow_duration, etc.
     */
    Map<String, Double> features();
}

package core.contracts;

import java.time.Instant;
import java.util.Map;

/**
 * A normalized representation of one unit of traffic
 * (CSV row now; PCAP packet later).
 */
public interface TrafficRecord {

    /** When this traffic event happened (or when it was captured). */
    Instant timestamp();

    /**
     * Raw fields as key-value pairs (keeps ingest flexible across CSV/PCAP).
     * Example keys: src_ip, dst_ip, src_port, dst_port, protocol, bytes, flags, etc.
     */
    Map<String, Object> fields();
}

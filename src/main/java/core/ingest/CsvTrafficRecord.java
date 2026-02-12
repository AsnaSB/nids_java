package core.ingest;

import core.contracts.TrafficRecord;

import java.time.Instant;
import java.util.Map;

public class CsvTrafficRecord implements TrafficRecord {

    private final Instant timestamp;
    private final Map<String, Object> fields;

    public CsvTrafficRecord(Instant timestamp, Map<String, Object> fields) {
        this.timestamp = timestamp;
        this.fields = fields;
    }

    @Override
    public Instant timestamp() {
        return timestamp;
    }

    @Override
    public Map<String, Object> fields() {
        return fields;
    }
}


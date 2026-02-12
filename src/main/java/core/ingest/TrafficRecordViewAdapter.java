package core.ingest;

import core.contracts.TrafficRecord;

public class TrafficRecordViewAdapter implements FlowRecordView {
    private final TrafficRecord record;

    public TrafficRecordViewAdapter(TrafficRecord record) {
        this.record = record;
    }

    @Override
    public String get(String columnName) {
        Object v = record.fields().get(columnName);
        return (v == null) ? null : String.valueOf(v);
    }
}

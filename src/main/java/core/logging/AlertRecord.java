package core.logging;

import java.time.LocalDateTime;
import java.util.List;

public class AlertRecord {

    private final LocalDateTime timestamp;
    private final int rowId;
    private final String label;
    private final double confidence;
    private final String severity;
    private final List<String> reasons;

    public AlertRecord(LocalDateTime timestamp,
                       int rowId,
                       String label,
                       double confidence,
                       String severity,
                       List<String> reasons) {
        this.timestamp = timestamp;
        this.rowId = rowId;
        this.label = label;
        this.confidence = confidence;
        this.severity = severity;
        this.reasons = reasons;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public int getRowId() { return rowId; }
    public String getLabel() { return label; }
    public double getConfidence() { return confidence; }
    public String getSeverity() { return severity; }
    public List<String> getReasons() { return reasons; }
}

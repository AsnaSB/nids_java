import java.util.*;

public class RuleEngine {

    public static class AlertRecord {
        public String predictedLabel;
        public List<String> reasons = new ArrayList<>();
        public double confidence;
        public String severity;
    }

    public List<AlertRecord> runRules(List<FlowFeatures> flows) {
        List<AlertRecord> alerts = new ArrayList<>();

        for (FlowFeatures flow : flows) {
            AlertRecord alert = new AlertRecord();
            int rulesTriggered = 0;

            // Rules
            if (flow.count > RuleConfig.COUNT_THRESHOLD) {
                alert.reasons.add("count > " + RuleConfig.COUNT_THRESHOLD);
                rulesTriggered++;
            }
            if (flow.serror_rate > RuleConfig.SERROR_RATE_THRESHOLD) {
                alert.reasons.add("serror_rate > " + RuleConfig.SERROR_RATE_THRESHOLD);
                rulesTriggered++;
            }
            if (flow.dst_host_count > RuleConfig.DST_HOST_COUNT_THRESHOLD) {
                alert.reasons.add("dst_host_count > " + RuleConfig.DST_HOST_COUNT_THRESHOLD);
                rulesTriggered++;
            }
            if (flow.rerror_rate > RuleConfig.RERROR_RATE_THRESHOLD) {
                alert.reasons.add("rerror_rate > " + RuleConfig.RERROR_RATE_THRESHOLD);
                rulesTriggered++;
            }

            // Confidence & severity
            if (rulesTriggered == 0) {
                alert.predictedLabel = "NORMAL";
                alert.confidence = 0.9;
                alert.severity = "NONE";
            } else if (rulesTriggered == 1) {
                alert.predictedLabel = "ATTACK";
                alert.confidence = 0.7;
                alert.severity = "LOW";
            } else if (rulesTriggered == 2) {
                alert.predictedLabel = "ATTACK";
                alert.confidence = 0.8;
                alert.severity = "MEDIUM";
            } else {
                alert.predictedLabel = "ATTACK";
                alert.confidence = 0.95;
                alert.severity = "HIGH";
            }

            alerts.add(alert);
        }

        return alerts;
    }
}

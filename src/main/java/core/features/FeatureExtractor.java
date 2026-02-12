package core.features;

import core.ingest.FlowRecordView;

import java.util.ArrayList;
import java.util.List;

public class FeatureExtractor {

    public FlowFeatures extract(FlowRecordView record) {

        List<String> missing = new ArrayList<>();

        int count = parseInt(record.get("count"), "count", missing, 0);
        double serrorRate = parseDouble(record.get("serror_rate"), "serror_rate", missing, 0.0);
        int dstHostCount = parseInt(record.get("dst_host_count"), "dst_host_count", missing, 0);
        double rerrorRate = parseDouble(record.get("rerror_rate"), "rerror_rate", missing, 0.0);

        boolean incomplete = !missing.isEmpty();

        return new FlowFeatures(
                count,
                serrorRate,
                dstHostCount,
                rerrorRate,
                incomplete,
                missing
        );
    }

    private int parseInt(String value, String fieldName, List<String> missing, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            missing.add(fieldName);
            return defaultValue;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            missing.add(fieldName);
            return defaultValue;
        }
    }

    private double parseDouble(String value, String fieldName, List<String> missing, double defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            missing.add(fieldName);
            return defaultValue;
        }

        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            missing.add(fieldName);
            return defaultValue;
        }
    }
}

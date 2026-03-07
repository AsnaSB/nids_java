package core.features;

import core.ingest.FlowRecordView;
import core.contracts.TrafficRecord;
import core.ingest.TrafficRecordViewAdapter;
import core.detection.FlowFeatures;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.*;

public class FeatureExtractor {

    private List<String> featureOrder;

    public FeatureExtractor() {
        loadFeatureSchema();
    }

    /*
     * ===============================
     * RULE ENGINE FEATURE EXTRACTION
     * ===============================
     */

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
                missing,
                null
        );
    }

    public FlowFeatures extract(TrafficRecord record) {
        return extract(new TrafficRecordViewAdapter(record));
    }

    /*
     * ===============================
     * ML FEATURE EXTRACTION
     * ===============================
     */

    public float[] extractMLFeatures(FlowRecordView record) {

        float[] vector = new float[featureOrder.size()];

        for (int i = 0; i < featureOrder.size(); i++) {

            String feature = featureOrder.get(i);

            // Numeric fields
            if (isNumericFeature(feature)) {

                vector[i] = parseFloat(record.get(feature));
            }

            // protocol one-hot
            else if (feature.startsWith("protocol_type_")) {

                String protocol = record.get("protocol_type");
                String value = feature.replace("protocol_type_", "");

                vector[i] = value.equals(protocol) ? 1f : 0f;
            }

            // service one-hot
            else if (feature.startsWith("service_")) {

                String service = record.get("service");
                String value = feature.replace("service_", "");

                vector[i] = value.equals(service) ? 1f : 0f;
            }

            // flag one-hot
            else if (feature.startsWith("flag_")) {

                String flag = record.get("flag");
                String value = feature.replace("flag_", "");

                vector[i] = value.equals(flag) ? 1f : 0f;
            }

            else {
                vector[i] = 0f;
            }
        }

        return vector;
    }

    public float[] extractMLFeatures(TrafficRecord record) {
        return extractMLFeatures(new TrafficRecordViewAdapter(record));
    }

    /*
     * ===============================
     * FEATURE SCHEMA LOADING
     * ===============================
     */

    private void loadFeatureSchema() {

        try {

            ObjectMapper mapper = new ObjectMapper();

            InputStream is = getClass().getClassLoader()
                    .getResourceAsStream("models/feature_schema.json");

            JsonNode root = mapper.readTree(is);

            featureOrder = new ArrayList<>();

            for (JsonNode node : root.get("features")) {

                featureOrder.add(node.asText());
            }

            System.out.println("Loaded ML feature schema. Total features: " + featureOrder.size());

        } catch (Exception e) {

            throw new RuntimeException("Failed to load feature schema", e);
        }
    }

    /*
     * ===============================
     * HELPER METHODS
     * ===============================
     */

    private boolean isNumericFeature(String feature) {

        return !(feature.startsWith("protocol_type_")
                || feature.startsWith("service_")
                || feature.startsWith("flag_"));
    }

    private int parseInt(String value, String fieldName, List<String> missing, int defaultValue) {

        if (value == null || value.trim().isEmpty()) {
            missing.add(fieldName);
            return defaultValue;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
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
        } catch (Exception e) {
            missing.add(fieldName);
            return defaultValue;
        }
    }

    private float parseFloat(String value) {

        if (value == null || value.trim().isEmpty()) {
            return 0f;
        }

        try {
            return Float.parseFloat(value.trim());
        } catch (Exception e) {
            return 0f;
        }
    }
}
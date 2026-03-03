package core.features;

import core.ingest.FlowRecordView;
import core.detection.FlowFeatures;

import java.util.Map;

public class FeatureExtractorTest {

    public static void main(String[] args) {

        Map<String, String> fakeRow = Map.of(
                "count", "150",
                "serror_rate", "0.8",
                "dst_host_count", "250",
                "rerror_rate", "0.1"
        );

        FlowRecordView record = new FlowRecordView() {
            @Override
            public String get(String key) {
                return fakeRow.get(key);
            }
        };

        FeatureExtractor extractor = new FeatureExtractor();
        FlowFeatures features = extractor.extract(record);

        System.out.println("Count: " + features.count);
        System.out.println("Serror Rate: " + features.serror_rate);
        System.out.println("Dst Host Count: " + features.dst_host_count);
        System.out.println("Rerror Rate: " + features.rerror_rate);
        System.out.println("Incomplete: " + features.incomplete);
        System.out.println("Missing Fields: " + features.missing);
    }
}
package core.features;

import core.ingest.FlowRecordView;

import java.util.Map;

public class FeatureExtractorTest {

    public static void main(String[] args) {

        Map<String, String> fakeRow = Map.of(
                "count", "150",
                "serror_rate", "0.8",
                "dst_host_count", "250",
                "rerror_rate", "0.1"
        );

        FlowRecordView record = fakeRow::get;

        FeatureExtractor extractor = new FeatureExtractor();
        FlowFeatures features = extractor.extract(record);

        System.out.println("Count: " + features.count);
        System.out.println("Serror Rate: " + features.serrorRate);
        System.out.println("Dst Host Count: " + features.dstHostCount);
        System.out.println("Rerror Rate: " + features.rerrorRate);
        System.out.println("Incomplete: " + features.incomplete);
        System.out.println("Missing Fields: " + features.missingFields);
    }
}

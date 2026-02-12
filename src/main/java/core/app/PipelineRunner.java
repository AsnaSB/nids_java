package core.app;

import core.contracts.TrafficRecord;
import core.features.FeatureExtractor;
import core.features.FlowFeatures;
import core.detection.RuleEngine;
import core.detection.RuleDecision;

import java.util.ArrayList;
import java.util.List;

public class PipelineRunner {

    private final CsvDatasetLoader loader = new CsvDatasetLoader();
    private final FeatureExtractor extractor = new FeatureExtractor();
    private final RuleEngine engine = new RuleEngine();

    public List<RuleDecision> run(String filePath) {

        List<TrafficRecord> records = loader.load(filePath);
        List<RuleDecision> results = new ArrayList<>();

        for (TrafficRecord record : records) {

            FlowFeatures features = extractor.extract(record);
            RuleDecision decision = engine.evaluate(features);

            results.add(decision);
        }

        return results;
    }
}

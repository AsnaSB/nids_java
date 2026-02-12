package core.app;

import core.contracts.TrafficRecord;
import core.features.FeatureExtractor;
import core.features.FlowFeatures;
import core.detection.RuleEngine;
import core.detection.RuleDecision;
import core.logging.AlertLogger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PipelineRunner {

    private final CsvDatasetLoader loader = new CsvDatasetLoader();
    private final FeatureExtractor extractor = new FeatureExtractor();
    private final RuleEngine engine = new RuleEngine();
    private final AlertLogger logger = new AlertLogger();

    public List<RuleDecision> run(String filePath) {

        List<TrafficRecord> records = loader.load(filePath);
        List<RuleDecision> results = new ArrayList<>();

        int rowId = 0;

        for (TrafficRecord record : records) {

            // Step 1: Extract features
            FlowFeatures features = extractor.extract(record);

            // Step 2: Run detection
            RuleDecision decision = engine.evaluate(features);

            results.add(decision);

            // Step 3: Log only ATTACK traffic
            if ("ATTACK".equals(decision.getLabel())) {

                String json = String.format(
                        "{\"time\":\"%s\",\"rowId\":%d,\"label\":\"%s\",\"confidence\":%.2f}",
                        LocalDateTime.now(),
                        rowId,
                        decision.getLabel(),
                        decision.getConfidence()
                );

                logger.log(json);
            }

            rowId++;
        }

        return results;
    }
}

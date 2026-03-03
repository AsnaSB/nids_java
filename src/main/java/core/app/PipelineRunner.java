package core.app;

import core.contracts.TrafficRecord;
import core.features.FeatureExtractor;
import core.detection.FlowFeatures;
import core.detection.RuleEngine;
import core.logging.AlertLogger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PipelineRunner {

    private final CsvDatasetLoader loader = new CsvDatasetLoader();
    private final FeatureExtractor extractor = new FeatureExtractor();
    private final RuleEngine engine = new RuleEngine();
    private final AlertLogger logger = new AlertLogger();

    public List<RuleEngine.AlertRecord> run(String filePath) {

        List<TrafficRecord> records = loader.load(filePath);
        List<RuleEngine.AlertRecord> results = new ArrayList<>();

        int rowId = 0;

        for (TrafficRecord record : records) {

            // Step 1: Extract features
            FlowFeatures features = extractor.extract(record);

            // Step 2: Run detection
            // Step 2: Run detection
            List<FlowFeatures> tempList = new ArrayList<>();
            tempList.add(features);

            List<RuleEngine.AlertRecord> alerts =
                    engine.runRules(tempList);

            RuleEngine.AlertRecord decision = alerts.get(0);
            results.add(decision);

            // Step 3: Log only ATTACK traffic
            if ("ATTACK".equals(decision.predictedLabel)) {

                String json = String.format(
                        "{\"time\":\"%s\",\"rowId\":%d,\"label\":\"%s\",\"confidence\":%.2f}",
                        LocalDateTime.now(),
                        rowId,
                        decision.predictedLabel,
                        decision.confidence
                );

                logger.log(json);
            }

            rowId++;
        }

        return results;
    }
}
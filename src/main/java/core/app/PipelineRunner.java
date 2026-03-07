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

    // Pipeline components
    private final CsvDatasetLoader loader;
    private final FeatureExtractor extractor;
    private final RuleEngine ruleEngine;
    private final AlertLogger logger;

    public PipelineRunner() {
        this.loader = new CsvDatasetLoader();
        this.extractor = new FeatureExtractor();
        this.ruleEngine = new RuleEngine();
        this.logger = new AlertLogger();
    }

    /**
     * Executes the full detection pipeline
     */
    public List<RuleEngine.AlertRecord> run(String filePath) {

        System.out.println("Loading dataset: " + filePath);

        // Step 1: Load dataset
        List<TrafficRecord> records = loader.load(filePath);

        System.out.println("Records loaded: " + records.size());

        List<RuleEngine.AlertRecord> results = new ArrayList<>();

        int rowId = 0;

        for (TrafficRecord record : records) {

            try {

                // Step 2: Extract features
                FlowFeatures features = extractor.extract(record);

                // Step 3: Run rule-based detection
                List<FlowFeatures> featureList = new ArrayList<>();
                featureList.add(features);

                List<RuleEngine.AlertRecord> alerts =
                        ruleEngine.runRules(featureList);

                if (alerts.isEmpty()) {
                    rowId++;
                    continue;
                }

                RuleEngine.AlertRecord decision = alerts.get(0);

                // Store decision
                results.add(decision);

                // Step 4: Log attack alerts
                if ("ATTACK".equals(decision.predictedLabel)) {

                    String json = String.format(
                            "{\"time\":\"%s\",\"rowId\":%d,\"label\":\"%s\",\"confidence\":%.2f,\"severity\":\"%s\"}",
                            LocalDateTime.now(),
                            rowId,
                            decision.predictedLabel,
                            decision.confidence,
                            decision.severity
                    );

                    logger.log(json);
                }

            } catch (Exception e) {

                System.err.println(
                        "Error processing record " + rowId + ": " + e.getMessage()
                );
            }

            rowId++;
        }

        System.out.println("Pipeline completed.");
        System.out.println("Total alerts generated: " + results.size());

        return results;
    }
}
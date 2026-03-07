package core.app;

import core.contracts.TrafficRecord;
import core.contracts.DetectionResult;
import core.features.FeatureExtractor;
import core.detection.FlowFeatures;
import core.detection.MLDetector;
import core.logging.AlertLogger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PipelineRunner {

    // Pipeline components
    private final CsvDatasetLoader loader;
    private final FeatureExtractor extractor;
    private final MLDetector detector;
    private final AlertLogger logger;

    public PipelineRunner() {

        this.loader = new CsvDatasetLoader();
        this.extractor = new FeatureExtractor();
        this.detector = new MLDetector();
        this.logger = new AlertLogger();
    }

    /**
     * Executes the full detection pipeline
     */
    public List<DetectionResult> run(String filePath) {

        System.out.println("Loading dataset: " + filePath);

        // Step 1: Load dataset
        List<TrafficRecord> records = loader.load(filePath);

        System.out.println("Records loaded: " + records.size());

        List<DetectionResult> results = new ArrayList<>();

        int rowId = 0;

        for (TrafficRecord record : records) {

            try {

                // Step 2: Extract features
                FlowFeatures features = extractor.extract(record);

                // Step 3: Run ML detection
                DetectionResult decision = detector.detect(features.mlVector);

                if (decision == null) {
                    rowId++;
                    continue;
                }

                // Step 4: Print prediction output
                System.out.println(
                        "Prediction → Row " + rowId +
                        " | Category: " + decision.getPredictedCategory() +
                        " | Attack: " + decision.getPredictedAttack() +
                        " | Confidence: " + decision.getConfidence()
                );

                // Store result
                results.add(decision);

                // Step 5: Log attack alerts
                if (!decision.getPredictedCategory().equalsIgnoreCase("normal")) {

                    String json = String.format(
                            "{\"time\":\"%s\",\"rowId\":%d,\"category\":\"%s\",\"attack\":\"%s\",\"confidence\":%.2f}",
                            LocalDateTime.now(),
                            rowId,
                            decision.getPredictedCategory(),
                            decision.getPredictedAttack(),
                            decision.getConfidence()
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
        System.out.println("Total records processed: " + results.size());

        return results;
    }
}
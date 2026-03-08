package core.detection;

import core.contracts.DetectionResult;

public class HybridDetector implements Detector {

    private final RuleEngine ruleEngine = new RuleEngine();
    private final HierarchicalDetector mlDetector = new HierarchicalDetector();
    @Override
public DetectionResult detect(double[] features) {

        if (features == null) {
            throw new RuntimeException("Features are null");
        }

        DetectionResult mlResult = mlDetector.detect(features);

            return mlResult;
    }
}
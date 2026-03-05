package core.detection;

import core.contracts.DetectionResult;

public class HybridDetector implements Detector {

    private final RuleEngine ruleEngine = new RuleEngine();
    private final MLDetector mlDetector = new MLDetector();

    @Override
    public DetectionResult detect(double[] features) {

        DetectionResult mlResult = mlDetector.detect(features);

        // simple hybrid strategy
        // ML result used as default

        return mlResult;
    }
}
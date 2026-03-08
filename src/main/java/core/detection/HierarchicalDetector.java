package core.detection;

import core.contracts.DetectionResult;
import java.util.HashMap;
import java.util.Map;

public class HierarchicalDetector implements Detector {

    private final CategoryClassifier categoryModel = new CategoryClassifier();
    private final AttackClassifier attackModel = new AttackClassifier();

    @Override
    public DetectionResult detect(double[] features) {

        String category = categoryModel.predict(features);

        String attack = attackModel.predict(category, features);

        double confidence = 0.85;

        Map<String, Double> categoryProbs = new HashMap<>();
        Map<String, Double> attackProbs = new HashMap<>();

        categoryProbs.put(category, confidence);
        attackProbs.put(attack, confidence);

        return new DetectionResult(
                category,
                attack,
                categoryProbs,
                attackProbs,
                confidence
        );
    }
}
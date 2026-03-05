package core.contracts;

import java.util.Map;

public class DetectionResult {

    private String predictedCategory;
    private String predictedAttack;

    private Map<String, Double> categoryProbabilities;
    private Map<String, Double> attackProbabilities;

    private double confidence;

    public DetectionResult(
            String predictedCategory,
            String predictedAttack,
            Map<String, Double> categoryProbabilities,
            Map<String, Double> attackProbabilities,
            double confidence
    ) {
        this.predictedCategory = predictedCategory;
        this.predictedAttack = predictedAttack;
        this.categoryProbabilities = categoryProbabilities;
        this.attackProbabilities = attackProbabilities;
        this.confidence = confidence;
    }

    public String getPredictedCategory() {
        return predictedCategory;
    }

    public String getPredictedAttack() {
        return predictedAttack;
    }

    public Map<String, Double> getCategoryProbabilities() {
        return categoryProbabilities;
    }

    public Map<String, Double> getAttackProbabilities() {
        return attackProbabilities;
    }

    public double getConfidence() {
        return confidence;
    }
}
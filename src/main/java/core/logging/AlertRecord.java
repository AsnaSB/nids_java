package core.logging;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class AlertRecord {

    private final LocalDateTime timestamp;
    private final int rowId;

    private final String predictedCategory;
    private final String predictedAttack;

    private final double confidence;

    private final Map<String, Double> categoryProbabilities;
    private final Map<String, Double> attackProbabilities;

    private final List<String> reasons;

    public AlertRecord(
            LocalDateTime timestamp,
            int rowId,
            String predictedCategory,
            String predictedAttack,
            double confidence,
            Map<String, Double> categoryProbabilities,
            Map<String, Double> attackProbabilities,
            List<String> reasons
    ) {
        this.timestamp = timestamp;
        this.rowId = rowId;
        this.predictedCategory = predictedCategory;
        this.predictedAttack = predictedAttack;
        this.confidence = confidence;
        this.categoryProbabilities = categoryProbabilities;
        this.attackProbabilities = attackProbabilities;
        this.reasons = reasons;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getRowId() {
        return rowId;
    }

    public String getPredictedCategory() {
        return predictedCategory;
    }

    public String getPredictedAttack() {
        return predictedAttack;
    }

    public double getConfidence() {
        return confidence;
    }

    public Map<String, Double> getCategoryProbabilities() {
        return categoryProbabilities;
    }

    public Map<String, Double> getAttackProbabilities() {
        return attackProbabilities;
    }

    public List<String> getReasons() {
        return reasons;
    }
}
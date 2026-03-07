package core.detection;

import core.contracts.DetectionResult;

import java.util.HashMap;
import java.util.Map;

public class MLDetector implements Detector {

    @Override
    public DetectionResult detect(double[] features) {

        // placeholder probabilities

        Map<String, Double> categoryProbs = new HashMap<>();

        categoryProbs.put("dos", 0.82);
        categoryProbs.put("probe", 0.09);
        categoryProbs.put("r2l", 0.05);
        categoryProbs.put("u2r", 0.03);
        categoryProbs.put("normal", 0.01);

        Map<String, Double> attackProbs = new HashMap<>();

        attackProbs.put("neptune", 0.71);
        attackProbs.put("smurf", 0.18);
        attackProbs.put("pod", 0.06);
        attackProbs.put("back", 0.05);

        return new DetectionResult(
                "dos",
                "neptune",
                categoryProbs,
                attackProbs,
                0.82
        );
    }
}
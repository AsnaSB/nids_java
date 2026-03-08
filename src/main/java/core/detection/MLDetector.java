package core.detection;

import core.contracts.DetectionResult;

import java.util.HashMap;
import java.util.Map;

public class MLDetector implements Detector {

    @Override
    public DetectionResult detect(double[] features) {

        if (features == null || features.length < 3) {
            throw new RuntimeException("Invalid ML feature vector");
        }

        Map<String, Double> categoryProbs = new HashMap<>();
        Map<String, Double> attackProbs = new HashMap<>();

        String category;
        String attack;
        double confidence;

        double duration = features[0];
        double srcBytes = features[1];
        double dstBytes = features[2];

        if (srcBytes > 10000 && dstBytes < 100) {

            category = "dos";
            attack = "neptune";
            confidence = 0.88;

        } else if (duration > 1000) {

            category = "probe";
            attack = "ipsweep";
            confidence = 0.76;

        } else if (dstBytes == 0 && srcBytes < 200) {

            category = "r2l";
            attack = "guess_passwd";
            confidence = 0.71;

        } else if (srcBytes < 50 && duration < 10) {

            category = "u2r";
            attack = "buffer_overflow";
            confidence = 0.67;

        } else {

            category = "normal";
            attack = "normal";
            confidence = 0.93;
        }

        categoryProbs.put("normal", category.equals("normal") ? confidence : 0.05);
        categoryProbs.put("dos", category.equals("dos") ? confidence : 0.05);
        categoryProbs.put("probe", category.equals("probe") ? confidence : 0.05);
        categoryProbs.put("r2l", category.equals("r2l") ? confidence : 0.05);
        categoryProbs.put("u2r", category.equals("u2r") ? confidence : 0.05);

        attackProbs.put(attack, confidence);

        return new DetectionResult(category, attack, categoryProbs, attackProbs, confidence);
    }
}
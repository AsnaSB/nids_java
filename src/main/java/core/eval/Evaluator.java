package core.eval;

import java.util.List;

public class Evaluator {
    // Non-static method
    public void evaluate(List<String> predictedLabels, List<String> actualLabels) {
        int tp = 0, tn = 0, fp = 0, fn = 0;

        // Compare each predicted vs actual
        for (int i = 0; i < predictedLabels.size(); i++) {
            if (predictedLabels.get(i).equals("ATTACK") && actualLabels.get(i).equals("ATTACK")) tp++;
            else if (predictedLabels.get(i).equals("NORMAL") && actualLabels.get(i).equals("NORMAL")) tn++;
            else if (predictedLabels.get(i).equals("ATTACK") && actualLabels.get(i).equals("NORMAL")) fp++;
            else if (predictedLabels.get(i).equals("NORMAL") && actualLabels.get(i).equals("ATTACK")) fn++;
        }

        int total = predictedLabels.size();
        double accuracy = (double)(tp + tn) / total;

        // ✅ Print metrics
        System.out.println("Accuracy: " + accuracy);
        System.out.println("TP: " + tp + ", TN: " + tn + ", FP: " + fp + ", FN: " + fn);
    }
}

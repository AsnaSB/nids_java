package core.eval;
import java.util.*;

public class Evaluator {

    public static class Metrics {
        public int TP, FP, TN, FN;
        public double accuracy;
    }

    public Metrics evaluate(List<String> predicted, List<String> actual) {
        Metrics metrics = new Metrics();

        for (int i = 0; i < predicted.size(); i++) {
            String p = predicted.get(i);
            String a = actual.get(i);

            if (p.equals("ATTACK") && a.equals("ATTACK")) metrics.TP++;
            else if (p.equals("ATTACK") && a.equals("NORMAL")) metrics.FP++;
            else if (p.equals("NORMAL") && a.equals("NORMAL")) metrics.TN++;
            else if (p.equals("NORMAL") && a.equals("ATTACK")) metrics.FN++;
        }

        metrics.accuracy = (double)(metrics.TP + metrics.TN) / predicted.size();
        return metrics;
    }
}

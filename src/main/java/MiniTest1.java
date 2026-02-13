import core.detection.FlowFeatures;
import core.detection.RuleEngine;
import core.eval.Evaluator;
import ui.DashboardScreen;

import java.util.*;

public class MiniTest1 {
    public static void main(String[] args) {
        // 1️⃣ Create sample FlowFeatures
        List<FlowFeatures> samples = new ArrayList<>();

        FlowFeatures sample1 = new FlowFeatures();
        sample1.count = 150;             // above threshold
        sample1.serror_rate = 0.6;       // above threshold
        sample1.dst_host_count = 250;    // above threshold
        sample1.rerror_rate = 0.1;       // below threshold
        samples.add(sample1);

        FlowFeatures sample2 = new FlowFeatures();
        sample2.count = 50;              // below threshold
        sample2.serror_rate = 0.2;       // below threshold
        sample2.dst_host_count = 100;    // below threshold
        sample2.rerror_rate = 0.3;       // below threshold
        samples.add(sample2);

        // 2️⃣ Run the RuleEngine
        RuleEngine engine = new RuleEngine();
        List<RuleEngine.AlertRecord> alerts = engine.runRules(samples);

        // 3️⃣ Print alerts
        for (int i = 0; i < alerts.size(); i++) {
            RuleEngine.AlertRecord alert = alerts.get(i);
            System.out.println("Sample " + (i + 1) + " Alert:");
            System.out.println("  Label: " + alert.predictedLabel);
            System.out.println("  Reasons: " + alert.reasons);
            System.out.println("  Confidence: " + alert.confidence);
            System.out.println("  Severity: " + alert.severity);
            System.out.println("---------------------------");
        }

        // 4️⃣ Run the Dashboard (optional GUI)
        DashboardScreen.main(null);
    }
}
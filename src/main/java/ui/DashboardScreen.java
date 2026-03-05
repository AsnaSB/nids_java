package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import core.app.PipelineRunner;
import core.detection.RuleDecision;

import java.util.List;

public class DashboardScreen {

    private VBox root;
    private Button runButton;
    private Label metricsLabel;

    public DashboardScreen() {

        root = new VBox(15);
        root.setPadding(new Insets(20));

        metricsLabel = new Label("Metrics will appear here");
        runButton = new Button("Run Evaluation on Test CSV");

        runButton.setOnAction(e -> {

            try {

                PipelineRunner runner = new PipelineRunner();

                // Call the correct method
                List<RuleDecision> results = runner.run("data/nsl_kdd_test_clean.csv");

                int attackCount = 0;
                int normalCount = 0;

                for (RuleDecision r : results) {
                    if ("ATTACK".equals(r.getLabel())) {
                        attackCount++;
                    } else {
                        normalCount++;
                    }
                }

                metricsLabel.setText(
                        "Total: " + results.size() +
                        " | ATTACK: " + attackCount +
                        " | NORMAL: " + normalCount
                );

            } catch (Exception ex) {

                metricsLabel.setText("Error running evaluation");
                ex.printStackTrace();
            }

        });

        root.getChildren().addAll(runButton, metricsLabel);
    }

    public VBox getView() {
        return root;
    }
}
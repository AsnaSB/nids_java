package ui;

import java.util.List;

import core.contracts.DetectionResult;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class HomeScreen extends StackPane {

    public HomeScreen(List<DetectionResult> results) {

        int totalRecords = 0;
        int attacksDetected = 0;

        if (results != null) {

            totalRecords = results.size();

            for (DetectionResult r : results) {
                if (!r.getPredictedCategory().equalsIgnoreCase("normal")) {
                    attacksDetected++;
                }
            }
        }

        // Threat level calculation
        String threatLevel = "LOW";

        if (totalRecords > 0) {
            double ratio = (double) attacksDetected / totalRecords;

            if (ratio > 0.4) {
                threatLevel = "HIGH";
            } else if (ratio > 0.15) {
                threatLevel = "MEDIUM";
            }
        }

        Label title = new Label("NIDS Dashboard");
        title.getStyleClass().add("title");

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(30);
        grid.setAlignment(Pos.CENTER);

        VBox totalRecordsCard = createCard("Total Records", String.valueOf(totalRecords));
        VBox attacksDetectedCard = createCard("Attacks Detected", String.valueOf(attacksDetected));
        VBox systemStatus = createCard("System Status", "RUNNING");
        VBox threatLevelCard = createCard("Threat Level", threatLevel);

        grid.add(totalRecordsCard, 0, 0);
        grid.add(attacksDetectedCard, 1, 0);
        grid.add(systemStatus, 0, 1);
        grid.add(threatLevelCard, 1, 1);

        VBox layout = new VBox(30, title, grid);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));

        getChildren().add(layout);
    }

    private VBox createCard(String labelText, String valueText) {

        Label label = new Label(labelText);
        Label value = new Label(valueText);

        value.setStyle("-fx-font-size:26px; -fx-font-weight:bold;");

        VBox card = new VBox(10, label, value);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(220, 120);

        card.getStyleClass().add("card");

        return card;
    }
}
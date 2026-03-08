package ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import core.contracts.DetectionResult;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class HomeScreen extends StackPane {

    public HomeScreen(List<DetectionResult> results) {

        int totalRecords = 0;
        int attacksDetected = 0;

        if (results != null) {
            totalRecords = results.size();

            for (DetectionResult r : results) {
                if (r.getPredictedCategory() != null &&
                        !r.getPredictedCategory().equalsIgnoreCase("normal")) {
                    attacksDetected++;
                }
            }
        }

        String threatLevel = "LOW";

        if (totalRecords > 0) {
            double ratio = (double) attacksDetected / totalRecords;

            if (ratio > 0.4) {
                threatLevel = "HIGH";
            } else if (ratio > 0.15) {
                threatLevel = "MEDIUM";
            }
        }

        setStyle("-fx-background-color: #f4f7fb;");

        Label title = new Label("NIDS Security Monitoring Dashboard");
        title.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #1f2937;"
        );

        String currentTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy  hh:mm a"));

        Label lastUpdated = new Label("Last Updated: " + currentTime);
        lastUpdated.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #6b7280;"
        );

        Label intro = new Label(
                "This system analyzes network traffic and detects potential cyber attacks " +
                "using machine learning models. Use the navigation menu to explore traffic data, " +
                "features, detection results, visualizations, and alerts."
        );

        intro.setWrapText(true);
        intro.setMaxWidth(850);
        intro.setAlignment(Pos.CENTER);

        intro.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #374151;" +
                "-fx-background-color: white;" +
                "-fx-padding: 15px;" +
                "-fx-background-radius: 12px;" +
                "-fx-border-radius: 12px;" +
                "-fx-border-color: #d1d5db;"
        );

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(30);
        grid.setAlignment(Pos.CENTER);

        VBox totalRecordsCard = createCard(
                "📊 Total Records",
                String.valueOf(totalRecords),
                "#ffffff"
        );

        VBox attacksDetectedCard = createCard(
                "🚨 Attacks Detected",
                String.valueOf(attacksDetected),
                "#ffffff"
        );

        VBox systemStatusCard = createCard(
                "⚙ System Status",
                "RUNNING",
                "#ffffff"
        );

        VBox threatLevelCard = createCard(
                "⚠ Threat Level",
                threatLevel,
                getThreatColor(threatLevel)
        );

        grid.add(totalRecordsCard, 0, 0);
        grid.add(attacksDetectedCard, 1, 0);
        grid.add(systemStatusCard, 0, 1);
        grid.add(threatLevelCard, 1, 1);

        VBox layout = new VBox(20, title, lastUpdated, intro, grid);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));

        getChildren().add(layout);
    }

    private VBox createCard(String labelText, String valueText, String color) {

        Label label = new Label(labelText);
        label.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #374151;"
        );

        Label value = new Label(valueText);
        value.setStyle(
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #111827;"
        );

        VBox card = new VBox(10, label, value);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(240, 130);
        card.setPadding(new Insets(15));

        card.setStyle(
                "-fx-background-color: " + color + ";" +
                "-fx-background-radius: 16px;" +
                "-fx-border-radius: 16px;" +
                "-fx-border-color: #e5e7eb;" +
                "-fx-border-width: 1;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 10, 0.2, 0, 4);"
        );

        return card;
    }

    private String getThreatColor(String level) {

        switch (level.toUpperCase()) {

            case "HIGH":
                return "#fecaca";

            case "MEDIUM":
                return "#fde68a";

            default:
                return "#bbf7d0";
        }
    }
}
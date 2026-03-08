package ui;

import core.contracts.DetectionResult;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashboardScreen extends VBox {

    public DashboardScreen(List<DetectionResult> results) {

        setSpacing(20);
        setPadding(new Insets(25));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: #f4f7fb;");

        // ===== Title =====
        Label title = new Label("NIDS Security Monitoring Dashboard");
        title.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #1f2937;"
        );

        // ===== Last Updated =====
        String currentTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy  hh:mm a"));

        Label lastUpdated = new Label("Last Updated: " + currentTime);
        lastUpdated.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #6b7280;"
        );

        // ===== Introduction =====
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

        // ===== Dashboard Data =====
        int totalRecords = results != null ? results.size() : 0;
        int attacksDetected = 0;

        if (results != null) {
            for (DetectionResult result : results) {
                String category = result.getPredictedCategory();
                if (category != null && !category.equalsIgnoreCase("normal")) {
                    attacksDetected++;
                }
            }
        }

        String systemStatus = attacksDetected > 0 ? "Monitoring Active" : "No Threat Detected";
        String threatLevel;

        if (attacksDetected == 0) {
            threatLevel = "Low";
        } else if (attacksDetected < 10) {
            threatLevel = "Moderate";
        } else {
            threatLevel = "High";
        }

        // ===== Cards =====
        GridPane cardsGrid = new GridPane();
        cardsGrid.setHgap(20);
        cardsGrid.setVgap(20);
        cardsGrid.setAlignment(Pos.CENTER);

        VBox totalCard = createCard("📊 Total Records", String.valueOf(totalRecords), "#2563eb");
        VBox attackCard = createCard("🚨 Attacks Detected", String.valueOf(attacksDetected), "#dc2626");
        VBox statusCard = createCard("⚙ System Status", systemStatus, "#16a34a");
        VBox threatCard = createCard("⚠ Threat Level", threatLevel, "#ea580c");

        cardsGrid.add(totalCard, 0, 0);
        cardsGrid.add(attackCard, 1, 0);
        cardsGrid.add(statusCard, 0, 1);
        cardsGrid.add(threatCard, 1, 1);

        getChildren().addAll(title, lastUpdated, intro, cardsGrid);
    }

    private VBox createCard(String heading, String value, String color) {
        Label headingLabel = new Label(heading);
        headingLabel.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;"
        );

        Label valueLabel = new Label(value);
        valueLabel.setStyle(
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;"
        );

        VBox card = new VBox(12, headingLabel, valueLabel);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20));
        card.setPrefSize(280, 120);

        card.setBackground(new Background(new BackgroundFill(
                Color.web(color), new CornerRadii(16), Insets.EMPTY
        )));

        card.setStyle(
                "-fx-background-radius: 16px;" +
                "-fx-border-radius: 16px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0.2, 0, 4);"
        );

        return card;
    }
}
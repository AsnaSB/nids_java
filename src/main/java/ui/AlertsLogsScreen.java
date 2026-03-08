package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AlertsLogsScreen extends StackPane {

    private static final int MAX_ALERTS = 100;

    public AlertsLogsScreen() {

        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Alerts / Logs");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        Label info = new Label("Showing latest " + MAX_ALERTS + " alerts");
        info.setStyle("-fx-font-size: 13px; -fx-text-fill: #6b7280;");

        VBox alertsBox = new VBox(10);
        alertsBox.setPadding(new Insets(10));

        File file = new File("logs/alerts.jsonl");

        List<String> lastAlerts = new LinkedList<>();

        String highestRiskCategory = "None";
        String highestRiskAttack = "None";
        double highestConfidence = -1;

        if (file.exists()) {

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

                String line;

                while ((line = reader.readLine()) != null) {

                    lastAlerts.add(line);

                    if (lastAlerts.size() > MAX_ALERTS) {
                        lastAlerts.remove(0);
                    }
                }

                if (!lastAlerts.isEmpty()) {

                    for (String alert : lastAlerts) {

                        String category = extractValue(alert, "category");
                        String attack = extractValue(alert, "attack");
                        String confidenceText = extractValue(alert, "confidence");

                        try {
                            double confidence = Double.parseDouble(confidenceText);

                            if (confidence > highestConfidence) {
                                highestConfidence = confidence;
                                highestRiskCategory = category.toUpperCase();
                                highestRiskAttack = attack;
                            }

                        } catch (NumberFormatException ignored) {
                        }
                    }

                    for (int i = lastAlerts.size() - 1; i >= 0; i--) {

                        String alert = lastAlerts.get(i);

                        Label alertLabel = new Label(formatAlert(alert));
                        alertLabel.setMaxWidth(Double.MAX_VALUE);
                        alertLabel.setStyle(getAlertStyle(alert));

                        String finalAlert = alert;
                        alertLabel.setOnMouseEntered(e ->
                                alertLabel.setStyle(getAlertStyle(finalAlert) + "-fx-opacity: 0.88;"));
                        alertLabel.setOnMouseExited(e ->
                                alertLabel.setStyle(getAlertStyle(finalAlert)));

                        alertsBox.getChildren().add(alertLabel);
                    }

                } else {

                    Label emptyLabel = new Label("No alerts detected.");
                    emptyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6b7280;");
                    alertsBox.getChildren().add(emptyLabel);
                }

            } catch (IOException e) {

                Label errorLabel = new Label("Error reading alerts.");
                errorLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #b91c1c;");
                alertsBox.getChildren().add(errorLabel);
            }

        } else {

            Label noFileLabel = new Label("No alerts detected.");
            noFileLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6b7280;");
            alertsBox.getChildren().add(noFileLabel);
        }

        Label summary;

        if (highestConfidence >= 0) {
            summary = new Label(
                    "Highest Risk Detected: "
                            + highestRiskCategory
                            + " (" + highestRiskAttack + ")"
                            + "  —  Confidence: "
                            + String.format("%.2f", highestConfidence)
            );
        } else {
            summary = new Label("Highest Risk Detected: None");
        }

       summary.setStyle(
        "-fx-font-size: 15px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: #1f2937;" +
        "-fx-background-color: #f3f4f6;" +
        "-fx-padding: 8 15 8 15;" +
        "-fx-background-radius: 8;"
);
        summary.setPadding(new Insets(5, 0, 10, 0));

        ScrollPane scrollPane = new ScrollPane(alertsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);

        container.getChildren().addAll(title, info, summary, scrollPane);

        getChildren().add(container);
    }

    private String formatAlert(String jsonLine) {

        String time = extractValue(jsonLine, "time").replace("T", " ");
        if (time.length() > 19) {
    time = time.substring(0, 19);
    }
        String category = extractValue(jsonLine, "category").toUpperCase();
        String attack = extractValue(jsonLine, "attack");
        String confidence = extractValue(jsonLine, "confidence");

        return time + "   |   "
                + category
                + "   |   ⚠ " + attack
                + "   |   Confidence: " + confidence;
    }

    private String extractValue(String jsonLine, String key) {

        String search = "\"" + key + "\":";
        int start = jsonLine.indexOf(search);

        if (start == -1) {
            return "";
        }

        start += search.length();

        while (start < jsonLine.length() && Character.isWhitespace(jsonLine.charAt(start))) {
            start++;
        }

        if (start < jsonLine.length() && jsonLine.charAt(start) == '"') {
            start++;
            int end = jsonLine.indexOf('"', start);
            if (end != -1) {
                return jsonLine.substring(start, end);
            }
        } else {
            int end = jsonLine.indexOf(',', start);
            if (end == -1) {
                end = jsonLine.indexOf('}', start);
            }
            if (end != -1) {
                return jsonLine.substring(start, end).trim();
            }
        }

        return "";
    }

    private String getAlertStyle(String alert) {

    double confidence = 0;

    try {
        String conf = extractValue(alert, "confidence");
        confidence = Double.parseDouble(conf);
    } catch (Exception ignored) {}

    String baseStyle =
            "-fx-font-family: 'Consolas';" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 8 12 8 12;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 4, 0, 0, 1);";

    if (confidence >= 0.85) {
        return baseStyle +
                "-fx-text-fill: #7f1d1d;" +
                "-fx-background-color: #fee2e2;" +
                "-fx-border-color: #ef4444;";
    }
    else if (confidence >= 0.70) {
        return baseStyle +
                "-fx-text-fill: #9a3412;" +
                "-fx-background-color: #ffedd5;" +
                "-fx-border-color: #f97316;";
    }
    else if (confidence >= 0.60) {
        return baseStyle +
                "-fx-text-fill: #92400e;" +
                "-fx-background-color: #fef3c7;" +
                "-fx-border-color: #f59e0b;";
    }
    else {
        return baseStyle +
                "-fx-text-fill: #1e3a8a;" +
                "-fx-background-color: #dbeafe;" +
                "-fx-border-color: #3b82f6;";
    }
}
}
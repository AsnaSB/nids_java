package ui;

import core.contracts.DetectionResult;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisualizationScreen extends VBox {

    public VisualizationScreen(List<DetectionResult> results) {
        setSpacing(20);
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #f4f4f4;");

        Label pageTitle = new Label("Attack Analytics & Visualization");
        pageTitle.getStyleClass().add("title");

        // ------------------------------
        // 1. Summary Cards
        // ------------------------------
        HBox summaryBar = createSummaryBar(results);

        // ------------------------------
        // 2. Charts Grid
        // ------------------------------
        HBox chartsRow = new HBox(20);
        chartsRow.setAlignment(Pos.CENTER);
        chartsRow.setPrefHeight(600);

        PieChart categoryChart = createCategoryChart(results);
        BarChart<String, Number> attackChart = createAttackChart(results);
        BarChart<String, Number> severityChart = createSeverityChart(results);

        VBox leftColumn = new VBox(20, categoryChart, severityChart);
        VBox rightColumn = new VBox(20, attackChart);

        HBox.setHgrow(leftColumn, Priority.ALWAYS);
        HBox.setHgrow(rightColumn, Priority.ALWAYS);
        VBox.setVgrow(categoryChart, Priority.ALWAYS);
        VBox.setVgrow(severityChart, Priority.ALWAYS);
        VBox.setVgrow(attackChart, Priority.ALWAYS);

        chartsRow.getChildren().addAll(leftColumn, rightColumn);

        getChildren().addAll(pageTitle, summaryBar, chartsRow);
    }

    // ------------------------------
    // Summary Cards
    // ------------------------------
    private HBox createSummaryBar(List<DetectionResult> results) {
        int totalAttacks = results.size();
        int highSeverity = (int) results.stream().filter(r -> r.getConfidence() > 0.85).count();
        String mostFrequentAttack = results.stream()
                .map(DetectionResult::getPredictedAttack)
                .reduce(new HashMap<String, Integer>(), (map, attack) -> {
                    map.put(attack, map.getOrDefault(attack, 0) + 1);
                    return map;
                }, (a, b) -> a)
                .entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("N/A");

        HBox bar = new HBox(20);
        bar.setAlignment(Pos.CENTER);

        bar.getChildren().addAll(
                createCard("Total Attacks", String.valueOf(totalAttacks), Color.web("#3498db")),
                createCard("High Severity", String.valueOf(highSeverity), Color.web("#e74c3c")),
                createCard("Most Frequent", mostFrequentAttack, Color.web("#f1c40f"))
        );

        return bar;
    }

    private VBox createCard(String title, String value, Color color) {
        VBox card = new VBox(5);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font(14));
        titleLabel.setTextFill(Color.GRAY);

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font(22));
        valueLabel.setTextFill(color);

        card.getChildren().addAll(titleLabel, valueLabel);
        card.setPrefWidth(180);
        card.setPrefHeight(80);

        return card;
    }

    // ------------------------------
    // Attack Category Pie Chart
    // ------------------------------
    private PieChart createCategoryChart(List<DetectionResult> results) {
        Map<String, Integer> counts = new HashMap<>();
        for (DetectionResult r : results) {
            counts.put(r.getPredictedCategory(), counts.getOrDefault(r.getPredictedCategory(), 0) + 1);
        }

        PieChart chart = new PieChart(FXCollections.observableArrayList());
        chart.setTitle("Attack Category Distribution");
        chart.setLegendVisible(true);

        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            chart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        chart.setLabelsVisible(true);
        chart.setPrefHeight(300);

        return chart;
    }

    // ------------------------------
    // Attack Type Bar Chart
    // ------------------------------
    private BarChart<String, Number> createAttackChart(List<DetectionResult> results) {
        Map<String, Integer> counts = new HashMap<>();
        for (DetectionResult r : results) {
            counts.put(r.getPredictedAttack(), counts.getOrDefault(r.getPredictedAttack(), 0) + 1);
        }

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Attack Type");
        yAxis.setLabel("Count");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Attack Type Distribution");
        chart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        counts.forEach((k, v) -> series.getData().add(new XYChart.Data<>(k, v)));
        chart.getData().add(series);
        chart.setPrefHeight(600);

        return chart;
    }

    // ------------------------------
    // Severity Distribution Bar Chart
    // ------------------------------
    private BarChart<String, Number> createSeverityChart(List<DetectionResult> results) {
        Map<String, Integer> counts = new HashMap<>();

        for (DetectionResult r : results) {
            double c = r.getConfidence();
            String severity;
            if (c > 0.85) severity = "HIGH";
            else if (c > 0.65) severity = "MEDIUM";
            else if (c > 0.4) severity = "LOW";
            else severity = "NONE";

            counts.put(severity, counts.getOrDefault(severity, 0) + 1);
        }

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Severity");
        yAxis.setLabel("Count");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Severity Distribution");
        chart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        counts.forEach((k, v) -> series.getData().add(new XYChart.Data<>(k, v)));
        chart.getData().add(series);
        chart.setPrefHeight(300);

        return chart;
    }
}
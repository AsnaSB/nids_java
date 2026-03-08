package ui;

import core.contracts.DetectionResult;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DetectionResultsScreen extends BorderPane {

    private TableView<DetectionResult> table;
    private TextArea detailsArea;
    private VBox summaryBox;

    private List<DetectionResult> results;

    public DetectionResultsScreen(List<DetectionResult> results) {

        this.results = results;

        setPadding(new Insets(20));

        summaryBox = new VBox(8);
        summaryBox.setPadding(new Insets(10));
        summaryBox.setAlignment(Pos.CENTER_LEFT);

        buildCategorySummary();

        setTop(summaryBox);

        buildTable();

        buildDetailsPanel();
    }

    // ===============================
    // SUMMARY LEVEL-1
    // ===============================

    private void buildCategorySummary() {

    summaryBox.getChildren().clear();

    Label title = new Label("Attack Category Summary");
    title.setStyle("-fx-font-size:16px; -fx-font-weight:bold;");

    summaryBox.getChildren().add(title);

    int total = results.size();

    Map<String, Long> categoryCounts =
            results.stream()
                    .collect(Collectors.groupingBy(
                            DetectionResult::getPredictedCategory,
                            Collectors.counting()
                    ));

    categoryCounts.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {

                String category = entry.getKey();
                long count = entry.getValue();

                double percent = (count * 100.0) / total;

                Label row = new Label(
                        category.toUpperCase()
                                + " : "
                                + count
                                + " ("
                                + String.format("%.2f", percent)
                                + "%)"
                );

                row.setStyle("-fx-font-size:14px;");
                summaryBox.getChildren().add(row);
            });

    Button breakdownBtn = new Button("View Attack Breakdown");

    breakdownBtn.setOnAction(e -> buildAttackBreakdown());

    summaryBox.getChildren().add(breakdownBtn);
}

    // ===============================
    // SUMMARY LEVEL-2
    // ===============================

    private void buildAttackBreakdown() {

        summaryBox.getChildren().clear();

        Label title = new Label("Level-2 Attack Breakdown");
        title.setStyle("-fx-font-size:16px; -fx-font-weight:bold;");

        summaryBox.getChildren().add(title);

        Map<String, Long> attackCounts =
                results.stream()
                        .filter(r ->
                                !r.getPredictedAttack()
                                        .equalsIgnoreCase("normal"))
                        .collect(Collectors.groupingBy(
                                DetectionResult::getPredictedAttack,
                                Collectors.counting()
                        ));

        attackCounts.forEach((attack, count) -> {

            Label row = new Label(attack + " : " + count);
            row.setStyle("-fx-font-size:14px;");
            summaryBox.getChildren().add(row);
        });

        Button backBtn = new Button("Back to Category Summary");

        backBtn.setOnAction(e -> buildCategorySummary());

        summaryBox.getChildren().add(backBtn);
    }

    // ===============================
    // TABLE
    // ===============================

    private void buildTable() {

        table = new TableView<>();

        TableColumn<DetectionResult,String> categoryCol =
                new TableColumn<>("Predicted Category");

        categoryCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getPredictedCategory()
                ));

        TableColumn<DetectionResult,String> confidenceCol =
                new TableColumn<>("Confidence");

        confidenceCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        String.format("%.2f",
                                data.getValue().getConfidence())
                ));

        TableColumn<DetectionResult,String> top3Col =
                new TableColumn<>("Top-3 Attacks");

        top3Col.setCellValueFactory(data ->
                new SimpleStringProperty(
                        getTop3Attacks(
                                data.getValue().getAttackProbabilities()
                        )
                ));

        TableColumn<DetectionResult,String> severityCol =
                new TableColumn<>("Severity");

        severityCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        calculateSeverity(
                                data.getValue().getPredictedCategory(),
                                data.getValue().getConfidence()
                        )
                ));

        table.getColumns().addAll(
                categoryCol,
                confidenceCol,
                top3Col,
                severityCol
        );

        table.setItems(FXCollections.observableArrayList(results));

        setCenter(table);
    }

    // ===============================
    // DETAILS PANEL
    // ===============================

    private void buildDetailsPanel() {

        detailsArea = new TextArea();
        detailsArea.setEditable(false);
        detailsArea.setPrefHeight(200);

        table.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {

                    if (newVal != null) {
                        detailsArea.setText(buildDetails(newVal));
                    }

                });

        VBox bottomBox = new VBox(10);
        bottomBox.setPadding(new Insets(10));

        bottomBox.getChildren().addAll(
                new Label("Attack Probability Breakdown"),
                detailsArea
        );

        setBottom(bottomBox);
    }

    // ===============================
    // TOP-3 ATTACKS
    // ===============================

    private String getTop3Attacks(Map<String,Double> probs) {

        if (probs == null || probs.isEmpty())
            return "-";

        return probs.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(
                        Comparator.reverseOrder()))
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(", "));
    }

    // ===============================
    // SEVERITY
    // ===============================

    private String calculateSeverity(
            String category,
            double confidence) {

        if ("normal".equalsIgnoreCase(category))
            return "NONE";

        if (confidence > 0.85)
            return "HIGH";

        if (confidence > 0.65)
            return "MEDIUM";

        return "LOW";
    }

    // ===============================
    // DETAILS
    // ===============================

    private String buildDetails(DetectionResult r) {

        StringBuilder sb = new StringBuilder();

        sb.append("Predicted Category: ")
                .append(r.getPredictedCategory())
                .append("\n\n");

        sb.append("Predicted Attack: ")
                .append(r.getPredictedAttack())
                .append("\n\n");

        sb.append("Confidence: ")
                .append(r.getConfidence())
                .append("\n\n");

        sb.append("Attack Probabilities:\n");

        if (r.getAttackProbabilities() != null) {

            r.getAttackProbabilities().forEach((k,v) ->
                    sb.append(k)
                            .append(" : ")
                            .append(String.format("%.4f", v))
                            .append("\n"));
        }

        return sb.toString();
    }
}
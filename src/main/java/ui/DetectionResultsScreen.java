package ui;

import core.contracts.DetectionResult;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
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

    public DetectionResultsScreen(List<DetectionResult> results) {

        setPadding(new Insets(20));

        table = new TableView<>();

        // ===== Columns =====

        TableColumn<DetectionResult, String> categoryCol =
                new TableColumn<>("Predicted Category");

        categoryCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getPredictedCategory()
                ));

        TableColumn<DetectionResult, String> confidenceCol =
                new TableColumn<>("Confidence");

        confidenceCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        String.format("%.2f", data.getValue().getConfidence())
                ));

        TableColumn<DetectionResult, String> topAttackCol =
                new TableColumn<>("Top‑3 Attacks");

        topAttackCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        getTop3Attacks(data.getValue().getAttackProbabilities())
                ));

        TableColumn<DetectionResult, String> severityCol =
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
                topAttackCol,
                severityCol
        );

        table.setItems(FXCollections.observableArrayList(results));

        // ===== Details panel =====

        detailsArea = new TextArea();
        detailsArea.setEditable(false);
        detailsArea.setPrefHeight(200);

        table.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {

                    if (newVal != null) {
                        detailsArea.setText(buildDetails(newVal));
                    }
                }
        );

        VBox bottomBox = new VBox(10);
        bottomBox.setPadding(new Insets(10));
        bottomBox.getChildren().addAll(
                new Label("Attack Probability Breakdown"),
                detailsArea
        );

        setCenter(table);
        setBottom(bottomBox);
    }

    // ===== Top 3 Attacks =====

    private String getTop3Attacks(Map<String, Double> probs) {

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

    // ===== Severity =====

    private String calculateSeverity(String category, double confidence) {

        if ("normal".equalsIgnoreCase(category))
            return "NONE";

        if (confidence > 0.85)
            return "HIGH";

        if (confidence > 0.65)
            return "MEDIUM";

        return "LOW";
    }

    // ===== Details panel =====

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

            r.getAttackProbabilities().forEach((k, v) ->
                    sb.append(k)
                            .append(" : ")
                            .append(String.format("%.4f", v))
                            .append("\n"));
        }

        return sb.toString();
    }
}
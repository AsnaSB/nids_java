package ui;

import core.ingest.CsvFlowReader;
import core.ingest.FlowRecord;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class DataLoaderScreen extends BorderPane {

    private TableView<FlowRecord> table = new TableView<>();
    private Label statusLabel = new Label("No dataset loaded");

    public DataLoaderScreen() {

        Button loadTrainBtn = new Button("Load Train CSV");
        Button loadTestBtn = new Button("Load Test CSV");

        HBox buttonBar = new HBox(10, loadTrainBtn, loadTestBtn);
        buttonBar.setPadding(new Insets(10));

        // ===== TABLE COLUMNS =====

        TableColumn<FlowRecord, Number> durationCol = new TableColumn<>("Duration");
        durationCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getDuration()));

        TableColumn<FlowRecord, String> protocolCol = new TableColumn<>("Protocol");
        protocolCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getProtocol()));

        TableColumn<FlowRecord, String> serviceCol = new TableColumn<>("Service");
        serviceCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getService()));

        TableColumn<FlowRecord, String> flagCol = new TableColumn<>("Flag");
        flagCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getFlag()));

        TableColumn<FlowRecord, Number> srcBytesCol = new TableColumn<>("Src Bytes");
        srcBytesCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getSrcBytes()));

        TableColumn<FlowRecord, Number> dstBytesCol = new TableColumn<>("Dst Bytes");
        dstBytesCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getDstBytes()));

        TableColumn<FlowRecord, String> labelCol = new TableColumn<>("Attack Category");
        labelCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getLabel()));

        table.getColumns().addAll(
                durationCol,
                protocolCol,
                serviceCol,
                flagCol,
                srcBytesCol,
                dstBytesCol,
                labelCol
        );

        // ===== BUTTON ACTIONS =====

        loadTrainBtn.setOnAction(e -> loadCsv("data/train_hierarchial_ml.csv"));
        loadTestBtn.setOnAction(e -> loadCsv("data/test_hierarchial_ml.csv"));

        VBox topBox = new VBox(5, buttonBar, statusLabel);
        topBox.setPadding(new Insets(10));

        setTop(topBox);
        setCenter(table);
    }

    private void loadCsv(String path) {

        CsvFlowReader reader = new CsvFlowReader();

        List<FlowRecord> records = reader.read(path);

        if (records.isEmpty()) {
            statusLabel.setText("Dataset empty or failed to load");
            return;
        }

        table.getItems().setAll(
                records.subList(0, Math.min(30, records.size()))
        );

        statusLabel.setText(
                "Loaded rows: " + records.size()
                        + " | Skipped rows: " + reader.getSkippedRows()
        );
    }
}

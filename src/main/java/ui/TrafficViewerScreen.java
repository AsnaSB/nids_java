package ui;

import core.contracts.TrafficRecord;
import core.app.CsvDatasetLoader;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.List;

public class TrafficViewerScreen extends BorderPane {

    private final TableView<TrafficRecord> table;
    private final TextArea detailsArea;
    private final TextField searchField;
    private final CheckBox attacksOnlyCheckBox;

    private final Label totalLabel;
    private final Label tcpLabel;
    private final Label udpLabel;
    private final Label icmpLabel;

    public TrafficViewerScreen() {

        setPadding(new Insets(20));

        CsvDatasetLoader loader = new CsvDatasetLoader();
        List<TrafficRecord> records = loader.load("data/nsl_kdd_test_clean.csv");

        ObservableList<TrafficRecord> masterData = FXCollections.observableArrayList(records);

        Label pageTitle = new Label("Network Traffic Records");
        pageTitle.getStyleClass().add("title");

        Label title = new Label("Traffic Viewer");
        title.setFont(Font.font(22));

        searchField = new TextField();
        searchField.setPromptText("Search protocol, service, flag...");

        attacksOnlyCheckBox = new CheckBox("Show only attacks");

        HBox filterBar = new HBox(10, searchField, attacksOnlyCheckBox);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        totalLabel = createSummaryCard("Total", "0");
        tcpLabel = createSummaryCard("TCP", "0");
        udpLabel = createSummaryCard("UDP", "0");
        icmpLabel = createSummaryCard("ICMP", "0");

        HBox summaryBar = new HBox(15, totalLabel, tcpLabel, udpLabel, icmpLabel);
        summaryBar.setPadding(new Insets(10, 0, 10, 0));

        VBox topSection = new VBox(12, pageTitle, title, filterBar, summaryBar);
        topSection.setPadding(new Insets(0, 0, 15, 0));

        setTop(topSection);

        Region normalBox = new Region();
        normalBox.setPrefSize(14, 14);
        normalBox.setStyle("-fx-background-color:white; -fx-border-color:black;");

        Region suspiciousBox = new Region();
        suspiciousBox.setPrefSize(14, 14);
        suspiciousBox.setStyle("-fx-background-color:#ffd966; -fx-border-color:black;");

        Region attackBox = new Region();
        attackBox.setPrefSize(14, 14);
        attackBox.setStyle("-fx-background-color:#ff9999; -fx-border-color:black;");

        HBox normalLegend = new HBox(6, normalBox, new Label("Normal Traffic"));
        HBox suspiciousLegend = new HBox(6, suspiciousBox, new Label("Suspicious Connection (REJ, RSTO, S0)"));
        HBox attackLegend = new HBox(6, attackBox, new Label("Attack Traffic"));

        HBox legendBox = new HBox(25, normalLegend, suspiciousLegend, attackLegend);
        legendBox.setPadding(new Insets(5, 0, 10, 0));

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<TrafficRecord, Number> rowCol = new TableColumn<>("#");
        rowCol.setCellValueFactory(cell ->
                new ReadOnlyObjectWrapper<>(table.getItems().indexOf(cell.getValue()) + 1));
        rowCol.setMaxWidth(60);

        TableColumn<TrafficRecord, String> protocolCol = new TableColumn<>("Protocol");
        protocolCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().fields().get("protocol").toString()));

        TableColumn<TrafficRecord, String> serviceCol = new TableColumn<>("Service");
        serviceCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().fields().get("service").toString()));

        TableColumn<TrafficRecord, String> flagCol = new TableColumn<>("Flag");
        flagCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().fields().get("flag").toString()));

        table.getColumns().addAll(rowCol, protocolCol, serviceCol, flagCol);

        FilteredList<TrafficRecord> filteredData = new FilteredList<>(masterData, p -> true);

        Runnable updateFilter = () -> {

            String search = searchField.getText() == null ?
                    "" : searchField.getText().toLowerCase();

            filteredData.setPredicate(record -> {

                String protocol = safe(record.fields().get("protocol"));
                String service = safe(record.fields().get("service"));
                String flag = safe(record.fields().get("flag"));

                boolean matchesSearch =
                        search.isEmpty()
                                || protocol.contains(search)
                                || service.contains(search)
                                || flag.contains(search);

                return matchesSearch;
            });

            updateSummary(filteredData);
        };

        searchField.textProperty().addListener((obs, oldVal, newVal) -> updateFilter.run());
        attacksOnlyCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> updateFilter.run());

        table.setItems(filteredData);
        updateSummary(filteredData);

        table.setRowFactory(tv -> new TableRow<>() {

            @Override
            protected void updateItem(TrafficRecord item, boolean empty) {

                super.updateItem(item, empty);

                if (empty || item == null) {
                    setStyle("");
                    return;
                }

                String flag = item.fields().getOrDefault("flag", "").toString().toLowerCase();

                if (flag.equals("rej") || flag.equals("rsto") || flag.equals("s0")) {
                    setStyle("-fx-background-color: #ffe08a;");
                } else {
                    setStyle("");
                }
            }
        });

        Label detailsTitle = new Label("Connection Details");
        detailsTitle.setFont(Font.font(16));

        detailsArea = new TextArea();
        detailsArea.setEditable(false);
        detailsArea.setWrapText(true);
        detailsArea.setPrefRowCount(6);
        detailsArea.setPromptText("Click a row to view details...");

        VBox detailsBox = new VBox(8, detailsTitle, detailsArea);
        detailsBox.setPadding(new Insets(10, 0, 0, 0));

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {

            if (selected != null) {

                detailsArea.setText(
                        "Protocol : " + selected.fields().get("protocol") + "\n" +
                        "Service  : " + selected.fields().get("service") + "\n" +
                        "Flag     : " + selected.fields().get("flag")
                );

            } else {
                detailsArea.clear();
            }
        });

        VBox centerBox = new VBox(12, legendBox, table, detailsBox);
        VBox.setVgrow(table, Priority.ALWAYS);

        setCenter(centerBox);

        table.placeholderProperty().bind(
                Bindings.when(Bindings.isEmpty(filteredData))
                        .then(new Label("No traffic records found"))
                        .otherwise((Label) null)
        );
    }

    private Label createSummaryCard(String title, String value) {

        Label label = new Label(title + ": " + value);

        label.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #d0d0d0;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 10 16;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;"
        );

        return label;
    }

    private void updateSummary(List<TrafficRecord> records) {

        long total = records.size();

        long tcp = records.stream()
                .filter(r -> safe(r.fields().get("protocol")).equals("tcp"))
                .count();

        long udp = records.stream()
                .filter(r -> safe(r.fields().get("protocol")).equals("udp"))
                .count();

        long icmp = records.stream()
                .filter(r -> safe(r.fields().get("protocol")).equals("icmp"))
                .count();

        totalLabel.setText("Total: " + total);
        tcpLabel.setText("TCP: " + tcp);
        udpLabel.setText("UDP: " + udp);
        icmpLabel.setText("ICMP: " + icmp);
    }

    private String safe(Object value) {
        return value == null ? "" : value.toString().toLowerCase();
    }
}
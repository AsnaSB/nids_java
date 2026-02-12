package ui;

import core.ingest.CsvFlowReader;
import core.ingest.FlowRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class DataLoaderScreen extends BorderPane {

        private TableView<FlowRecord> table = new TableView<>();
        private Label statusLabel = new Label("No file loaded");

        public DataLoaderScreen() {

                Button loadTrainBtn = new Button("Load Train CSV");
                Button loadTestBtn = new Button("Load Test CSV");

                loadTrainBtn.setOnAction(e -> loadFile());
                loadTestBtn.setOnAction(e -> loadFile());

                HBox buttons = new HBox(10, loadTrainBtn, loadTestBtn);
                buttons.setAlignment(Pos.CENTER);

                createTableColumns();

                VBox top = new VBox(10, buttons, statusLabel);
                top.setAlignment(Pos.CENTER);
                top.setPadding(new javafx.geometry.Insets(10));

                setTop(top);
                setCenter(table);
        }

        private void createTableColumns() {

                TableColumn<FlowRecord, Integer> durationCol = new TableColumn<>("Duration");
                durationCol.setCellValueFactory(
                                c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getDuration())
                                                .asObject());

                TableColumn<FlowRecord, String> protocolCol = new TableColumn<>("Protocol");
                protocolCol.setCellValueFactory(
                                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getProtocolType()));

                TableColumn<FlowRecord, String> serviceCol = new TableColumn<>("Service");
                serviceCol.setCellValueFactory(
                                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getService()));

                TableColumn<FlowRecord, String> flagCol = new TableColumn<>("Flag");
                flagCol.setCellValueFactory(
                                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFlag()));

                TableColumn<FlowRecord, Integer> srcCol = new TableColumn<>("Src Bytes");
                srcCol.setCellValueFactory(
                                c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getSrcBytes())
                                                .asObject());

                TableColumn<FlowRecord, Integer> dstCol = new TableColumn<>("Dst Bytes");
                dstCol.setCellValueFactory(
                                c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getDstBytes())
                                                .asObject());

                TableColumn<FlowRecord, Integer> countCol = new TableColumn<>("Count");
                countCol.setCellValueFactory(
                                c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getCount())
                                                .asObject());

                TableColumn<FlowRecord, Integer> srvCountCol = new TableColumn<>("Srv Count");
                srvCountCol.setCellValueFactory(
                                c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getSrvCount())
                                                .asObject());

                TableColumn<FlowRecord, String> labelCol = new TableColumn<>("Label");
                labelCol.setCellValueFactory(
                                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getLabel()));

                table.getColumns().addAll(
                                durationCol, protocolCol, serviceCol, flagCol,
                                srcCol, dstCol, countCol, srvCountCol, labelCol);
        }

        private void loadFile() {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Select NSL-KDD CSV");

                // Suggest data directory
                File currentDir = new File(System.getProperty("user.dir"));
                File dataDir = new File(currentDir, "data");
                if (dataDir.exists()) {
                        chooser.setInitialDirectory(dataDir);
                }

                File file = chooser.showOpenDialog(getScene().getWindow());
                if (file == null)
                        return;

                CsvFlowReader reader = new CsvFlowReader();
                List<FlowRecord> records = reader.read(file.getAbsolutePath());

                int previewSize = Math.min(30, records.size());
                ObservableList<FlowRecord> preview = FXCollections.observableArrayList(records.subList(0, previewSize));

                table.setItems(preview);

                statusLabel.setText(
                                "Loaded rows: " + records.size() +
                                                " | Skipped: " + reader.getSkippedRows());
        }
}

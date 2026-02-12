package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class FeatureInspectorScreen extends BorderPane {

    // ---- Change this path if needed ----
    private static final String DEFAULT_CSV_PATH = "data/nsl_kdd_test_clean.csv";

    private final TextField pathField = new TextField(DEFAULT_CSV_PATH);
    private final Button loadBtn = new Button("Load CSV");
    private final Label loadStatus = new Label("No dataset loaded.");

    private final TextField indexField = new TextField("0");
    private final Button inspectBtn = new Button("Inspect Row");
    private final Label inspectStatus = new Label("");

    // Store header -> index
    private Map<String, Integer> colIndex = new HashMap<>();
    // Store all rows as String[] (fast + simple)
    private final List<String[]> rows = new ArrayList<>();

    private final GridPane grid = new GridPane();

    public FeatureInspectorScreen() {
        // Top: loader bar
        HBox top = new HBox(10, new Label("CSV Path:"), pathField, loadBtn);
        top.setPadding(new Insets(12));
        top.setAlignment(Pos.CENTER_LEFT);

        loadBtn.setOnAction(e -> loadCsv());

        VBox topBox = new VBox(8, top, loadStatus);
        topBox.setPadding(new Insets(10));

        // Center: inspector controls + grid
        HBox inspectorBar = new HBox(10, new Label("Row index:"), indexField, inspectBtn);
        inspectorBar.setPadding(new Insets(12));
        inspectorBar.setAlignment(Pos.CENTER_LEFT);

        inspectBtn.setOnAction(e -> inspectRow());

        grid.setHgap(12);
        grid.setVgap(8);
        grid.setPadding(new Insets(12));

        VBox centerBox = new VBox(8, inspectorBar, inspectStatus, new Separator(), grid);
        centerBox.setPadding(new Insets(10));

        setTop(topBox);
        setCenter(centerBox);

        // Optional: auto-load on screen open (comment out if you don’t want)
        // loadCsv();
    }

    private void loadCsv() {
        rows.clear();
        colIndex.clear();
        grid.getChildren().clear();

        String path = pathField.getText().trim();
        if (path.isEmpty()) {
            loadStatus.setText("Please enter a CSV path.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                loadStatus.setText("CSV is empty.");
                return;
            }

            String[] headers = splitCsvLine(headerLine);
            for (int i = 0; i < headers.length; i++) {
                colIndex.put(headers[i].trim(), i);
            }

            String line;
            int loaded = 0;
            while ((line = br.readLine()) != null) {
                String[] parts = splitCsvLine(line);
                // skip malformed lines
                if (parts.length != headers.length) continue;
                rows.add(parts);
                loaded++;
            }

            loadStatus.setText("Loaded rows: " + loaded + " | Columns: " + headers.length);
            inspectStatus.setText("Now inspect a row index (0.." + (rows.size() - 1) + ").");

        } catch (Exception ex) {
            loadStatus.setText("Failed to load CSV: " + ex.getMessage());
        }
    }

    private void inspectRow() {
        if (rows.isEmpty()) {
            inspectStatus.setText("No data loaded. Click 'Load CSV' first.");
            return;
        }

        int idx;
        try {
            idx = Integer.parseInt(indexField.getText().trim());
        } catch (Exception ex) {
            inspectStatus.setText("Invalid index number.");
            return;
        }

        if (idx < 0 || idx >= rows.size()) {
            inspectStatus.setText("Index out of range (0.." + (rows.size() - 1) + ").");
            return;
        }

        String[] row = rows.get(idx);

        // required columns for your Member-B work
        String rawCount = getCell(row, "count");
        String rawSerror = getCell(row, "serror_rate");
        String rawDstHostCount = getCell(row, "dst_host_count");
        String rawRerror = getCell(row, "rerror_rate");

        // parse safely (same logic as your extractor)
        List<String> missing = new ArrayList<>();
        int count = parseInt(rawCount, "count", missing, 0);
        double serrorRate = parseDouble(rawSerror, "serror_rate", missing, 0.0);
        int dstHostCount = parseInt(rawDstHostCount, "dst_host_count", missing, 0);
        double rerrorRate = parseDouble(rawRerror, "rerror_rate", missing, 0.0);

        boolean incomplete = !missing.isEmpty();

        // Update UI grid
        grid.getChildren().clear();
        int r = 0;

        r = addRow(r, "Raw count", rawCount);
        r = addRow(r, "Raw serror_rate", rawSerror);
        r = addRow(r, "Raw dst_host_count", rawDstHostCount);
        r = addRow(r, "Raw rerror_rate", rawRerror);

        r = addRow(r, "Parsed count", String.valueOf(count));
        r = addRow(r, "Parsed serrorRate", String.valueOf(serrorRate));
        r = addRow(r, "Parsed dstHostCount", String.valueOf(dstHostCount));
        r = addRow(r, "Parsed rerrorRate", String.valueOf(rerrorRate));

        r = addRow(r, "Incomplete?", String.valueOf(incomplete));
        r = addRow(r, "Missing fields", missing.toString());

        inspectStatus.setText("Row " + idx + " inspected.");
    }

    private String getCell(String[] row, String col) {
        Integer i = colIndex.get(col);
        if (i == null || i < 0 || i >= row.length) return null;
        String v = row[i];
        return v == null ? null : v.trim();
    }

    private int addRow(int row, String key, String val) {
        Label k = new Label(key + ":");
        k.setStyle("-fx-font-weight: bold;");
        Label v = new Label(val == null ? "(null)" : val);
        grid.add(k, 0, row);
        grid.add(v, 1, row);
        return row + 1;
    }

    private int parseInt(String v, String name, List<String> missing, int def) {
        if (v == null || v.isBlank()) {
            missing.add(name);
            return def;
        }
        try {
            return Integer.parseInt(v.trim());
        } catch (NumberFormatException e) {
            missing.add(name);
            return def;
        }
    }

    private double parseDouble(String v, String name, List<String> missing, double def) {
        if (v == null || v.isBlank()) {
            missing.add(name);
            return def;
        }
        try {
            return Double.parseDouble(v.trim());
        } catch (NumberFormatException e) {
            missing.add(name);
            return def;
        }
    }

    // Simple CSV splitting (works because your dataset fields don’t contain commas inside quotes)
    private String[] splitCsvLine(String line) {
        return line.split(",", -1);
    }
}

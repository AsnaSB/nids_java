package core.ingest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CsvFlowReader {

    private int skippedRows = 0;

    public List<FlowRecord> read(String filePath) {

        List<FlowRecord> records = new ArrayList<>();
        skippedRows = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String headerLine = br.readLine();

            if (headerLine == null)
                return records;

            // Read column names
            String[] headers = headerLine.split(",");
            Map<String, Integer> columnIndex = new HashMap<>();

            for (int i = 0; i < headers.length; i++) {
                columnIndex.put(headers[i].trim(), i);
            }

            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty())
                    continue;

                String[] parts = line.split(",");

                try {

                    int duration = parseInt(parts, columnIndex, "duration");
                    String protocol = parseString(parts, columnIndex, "protocol_type");
                    String service = parseString(parts, columnIndex, "service");
                    String flag = parseString(parts, columnIndex, "flag");

                    int srcBytes = parseInt(parts, columnIndex, "src_bytes");
                    int dstBytes = parseInt(parts, columnIndex, "dst_bytes");

                    String label = parseString(parts, columnIndex, "attack_category");

                    FlowRecord record = new FlowRecord(
                            duration,
                            protocol,
                            service,
                            flag,
                            srcBytes,
                            dstBytes,
                            label
                    );

                    records.add(record);

                } catch (Exception e) {
                    skippedRows++;
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }

        return records;
    }

    private int parseInt(String[] parts, Map<String, Integer> index, String column) {

        if (!index.containsKey(column))
            return 0;

        int pos = index.get(column);

        if (pos >= parts.length)
            return 0;

        try {
            return Integer.parseInt(parts[pos].trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String parseString(String[] parts, Map<String, Integer> index, String column) {

        if (!index.containsKey(column))
            return "";

        int pos = index.get(column);

        if (pos >= parts.length)
            return "";

        return parts[pos].trim();
    }

    public int getSkippedRows() {
        return skippedRows;
    }
}
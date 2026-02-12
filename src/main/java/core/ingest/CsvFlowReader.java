package core.ingest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvFlowReader {

    private int skippedRows = 0;

    public List<FlowRecord> read(String filePath) {
        List<FlowRecord> records = new ArrayList<>();
        skippedRows = 0; // Reset for each read

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;

            // read header row and ignore it
            String header = br.readLine();
            if (header == null)
                return records;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                String[] parts = line.split(",");

                // Expected 42 columns for NSL-KDD cleaned data
                if (parts.length < 42) {
                    skippedRows++;
                    continue;
                }

                try {
                    int duration = Integer.parseInt(parts[0].trim());
                    String protocolType = parts[1].trim();
                    String service = parts[2].trim();
                    String flag = parts[3].trim();
                    int srcBytes = Integer.parseInt(parts[4].trim());
                    int dstBytes = Integer.parseInt(parts[5].trim());
                    // Skip columns 6-21
                    int count = Integer.parseInt(parts[22].trim());
                    int srvCount = Integer.parseInt(parts[23].trim());
                    // Skip columns 24-40
                    String label = parts[41].trim();

                    records.add(new FlowRecord(duration, protocolType, service, flag, srcBytes, dstBytes, label));


                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    skippedRows++;
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }

        return records;
    }

    public int getSkippedRows() {
        return skippedRows;
    }
}

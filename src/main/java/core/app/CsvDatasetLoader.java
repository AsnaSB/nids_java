package core.app;

import core.contracts.TrafficRecord;
import core.ingest.CsvTrafficRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class CsvDatasetLoader {

    public List<TrafficRecord> load(String filePath) {

        List<TrafficRecord> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String headerLine = reader.readLine();
            if (headerLine == null) {
                return records;
            }

            String[] headers = headerLine.split(",");

            String line;
            while ((line = reader.readLine()) != null) {

                String[] values = line.split(",");

                Map<String, Object> fieldMap = new HashMap<>();

                for (int i = 0; i < headers.length && i < values.length; i++) {
                    fieldMap.put(headers[i].trim(), values[i].trim());
                }

                TrafficRecord record = new CsvTrafficRecord(
                        Instant.now(),
                        fieldMap
                );

                records.add(record);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return records;
    }
}

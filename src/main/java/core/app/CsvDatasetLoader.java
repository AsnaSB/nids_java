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
                    String header = headers[i].trim();
                    String value = values[i].trim();

                    // store original CSV header
                    fieldMap.put(header, value);

                    // add aliases for UI
                    if (header.equalsIgnoreCase("protocol_type")) {
                        fieldMap.put("protocol", value);
                    }
                    if (header.equalsIgnoreCase("service")) {
                        fieldMap.put("service", value);
                    }
                    if (header.equalsIgnoreCase("flag")) {
                        fieldMap.put("flag", value);
                    }
                    if (header.equalsIgnoreCase("src_bytes")) {
                        fieldMap.put("srcBytes", value);
                    }
                    if (header.equalsIgnoreCase("dst_bytes")) {
                        fieldMap.put("dstBytes", value);
                    }
                    if (header.equalsIgnoreCase("label")) {
                        fieldMap.put("label", value);
                    }
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
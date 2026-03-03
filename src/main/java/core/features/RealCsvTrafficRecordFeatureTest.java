package core.features;

import core.contracts.TrafficRecord;
import core.ingest.CsvTrafficRecord;
import core.detection.FlowFeatures;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class RealCsvTrafficRecordFeatureTest {
    public static void main(String[] args) throws Exception {

        String path = "data/nsl_kdd_test_clean.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String headerLine = br.readLine();
            String rowLine = br.readLine();

            if (headerLine == null || rowLine == null) {
                System.out.println("CSV is empty or missing rows.");
                return;
            }

            String[] headers = headerLine.split(",");
            String[] values = rowLine.split(",");

            Map<String, Object> fields = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                String key = headers[i];
                String val = (i < values.length) ? values[i] : "";
                fields.put(key, val);
            }

            TrafficRecord rec = new CsvTrafficRecord(Instant.now(), fields);

            FeatureExtractor fx = new FeatureExtractor();
            FlowFeatures f = fx.extract(rec); // ✅ uses your overload

            System.out.println("count=" + f.count);
            System.out.println("serrorRate=" + f.serror_rate);
            System.out.println("dstHostCount=" + f.dst_host_count);
            System.out.println("rerrorRate=" + f.rerror_rate);
            System.out.println("incomplete=" + f.incomplete);
            System.out.println("missing=" + f.missing);
        }
    }
}

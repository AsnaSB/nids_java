package core.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AlertLogger {

    private static final String LOG_FILE = "logs/alerts.jsonl";

    public AlertLogger() {
        File dir = new File("logs");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void log(String jsonLine) {

        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(jsonLine + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

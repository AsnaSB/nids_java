package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AlertsLogsScreen extends StackPane {

    public AlertsLogsScreen() {

        VBox container = new VBox(10);
        container.setAlignment(Pos.TOP_CENTER);
        container.setStyle("-fx-padding: 20;");

        Label title = new Label("Alerts / Logs");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        VBox logsBox = new VBox(5);

        File file = new File("logs/alerts.jsonl");

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    Label logLine = new Label(line);
                    logLine.setStyle("-fx-font-family: monospace;");
                    logsBox.getChildren().add(logLine);
                }

            } catch (IOException e) {
                logsBox.getChildren().add(new Label("Error reading logs."));
            }
        } else {
            logsBox.getChildren().add(new Label("No alerts yet."));
        }

        ScrollPane scrollPane = new ScrollPane(logsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);

        container.getChildren().addAll(title, scrollPane);

        getChildren().add(container);
    }
}

package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import ui.DataLoaderScreen;

public class MainLayout extends BorderPane {

    private StackPane contentArea = new StackPane();

    public MainLayout() {

        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(200);
        sidebar.setStyle("-fx-background-color: white;");

        Button homeBtn = createNavButton("🏠  Home");
        Button trafficBtn = createNavButton("📡  Traffic Viewer");
        Button resultsBtn = createNavButton("🤖  Detection Results");
        Button alertsBtn = createNavButton("🚨  Alerts / Logs");
        Button dataBtn = createNavButton("📂  Dataset Loader");

        sidebar.getChildren().addAll(homeBtn, trafficBtn, resultsBtn, alertsBtn, dataBtn);

        contentArea.setStyle("-fx-background-color: white;");

        setLeft(sidebar);
        setCenter(contentArea);

        showHome();

        homeBtn.setOnAction(e -> showHome());
        trafficBtn.setOnAction(e -> showTraffic());
        resultsBtn.setOnAction(e -> showResults());
        alertsBtn.setOnAction(e -> showAlerts());
        dataBtn.setOnAction(e -> contentArea.getChildren().setAll(new DataLoaderScreen()));
    }

    private Button createNavButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);

        String normal =
            "-fx-background-color: white;" +
            "-fx-text-fill: #111827;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10 12;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: black;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;";

        String hover =
            "-fx-background-color: #f9fafb;" +
            "-fx-text-fill: #111827;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10 12;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: black;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;";

        btn.setStyle(normal);

        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(normal));

        return btn;
    }

    private void showHome() {
        contentArea.getChildren().setAll(new HomeScreen());
    }

    private void showTraffic() {
        contentArea.getChildren().setAll(new TrafficViewerScreen());
    }

    private void showResults() {
        contentArea.getChildren().setAll(new DetectionResultsScreen());
    }

    private void showAlerts() {
        contentArea.getChildren().setAll(new AlertsLogsScreen());
    }
}

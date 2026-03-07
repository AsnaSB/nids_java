package ui;
import core.app.PipelineRunner;
import core.contracts.DetectionResult;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

public class MainLayout extends BorderPane {

    private final StackPane contentArea = new StackPane();

    public MainLayout() {

        // ===== Sidebar =====
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(200);
        sidebar.setStyle("-fx-background-color: white;");

        Button homeBtn = createNavButton("🏠  Home");
        Button trafficBtn = createNavButton("📡  Traffic Viewer");
        Button featureBtn = createNavButton("🧩  Feature Inspector"); // ✅ new
        Button resultsBtn = createNavButton("🤖  Detection Results");
        Button alertsBtn = createNavButton("🚨  Alerts / Logs");

        // ✅ Add buttons ONLY ONCE (and in correct order)
        sidebar.getChildren().addAll(homeBtn, trafficBtn, featureBtn, resultsBtn, alertsBtn);

        // ===== Content Area =====
        contentArea.setStyle("-fx-background-color: white;");

        setLeft(sidebar);
        setCenter(contentArea);

        // Default screen
        showHome();

        // Navigation
        homeBtn.setOnAction(e -> showHome());
        trafficBtn.setOnAction(e -> showTraffic());
        featureBtn.setOnAction(e -> showFeatureInspector()); // ✅ new
        resultsBtn.setOnAction(e -> showResults());
        alertsBtn.setOnAction(e -> showAlerts());
    }

    // ===== Button Style =====
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

    // ===== Screen Switching =====
    private void showHome() {
        contentArea.getChildren().setAll(new HomeScreen());
    }

    private void showTraffic() {
        contentArea.getChildren().setAll(new TrafficViewerScreen());
    }

    private void showFeatureInspector() {
        contentArea.getChildren().setAll(new FeatureInspectorScreen()); // ✅ new screen class
    }

    private void showResults() {

    core.app.PipelineRunner runner = new core.app.PipelineRunner();

    java.util.List<core.contracts.DetectionResult> results =
            runner.run("data/nsl_kdd_test_clean.csv");

    contentArea.getChildren().setAll(
            new DetectionResultsScreen(results)
    );
}

    private void showAlerts() {
        contentArea.getChildren().setAll(new AlertsLogsScreen());
    }
}

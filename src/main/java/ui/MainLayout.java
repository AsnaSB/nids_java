package ui;

import core.app.PipelineRunner;
import core.contracts.DetectionResult;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainLayout extends BorderPane {

    private final StackPane contentArea = new StackPane();
    private List<DetectionResult> cachedResults;

    public MainLayout() {

        getStyleClass().add("root");

        // Run detection pipeline once
        System.out.println("Initializing NIDS detection pipeline...");
        PipelineRunner runner = new PipelineRunner();
        cachedResults = runner.run("data/nsl_kdd_test_clean.csv");
        System.out.println("Pipeline initialization complete.");

        // =========================
        // Sidebar
        // =========================

        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(200);
        sidebar.getStyleClass().add("sidebar");

        Button homeBtn = createNavButton("🏠  Home");
        Button trafficBtn = createNavButton("📡  Traffic Viewer");
        Button featureBtn = createNavButton("🧩  Feature Inspector");
        Button resultsBtn = createNavButton("🤖  Detection Results");
        Button visualizationBtn = createNavButton("📊  Visualization");
        Button alertsBtn = createNavButton("🚨  Alerts / Logs");

        sidebar.getChildren().addAll(
                homeBtn,
                trafficBtn,
                featureBtn,
                resultsBtn,
                visualizationBtn,
                alertsBtn
        );

        // =========================
        // Content Area
        // =========================

        contentArea.getStyleClass().add("content-area");
        contentArea.setPadding(new Insets(20));

        setLeft(sidebar);
        setCenter(contentArea);

        // Default screen
        showHome();

        // =========================
        // Navigation
        // =========================

        homeBtn.setOnAction(e -> showHome());
        trafficBtn.setOnAction(e -> showTraffic());
        featureBtn.setOnAction(e -> showFeatureInspector());
        resultsBtn.setOnAction(e -> showResults());
        visualizationBtn.setOnAction(e -> showVisualization());
        alertsBtn.setOnAction(e -> showAlerts());
    }

    // =========================
    // Button Style
    // =========================

    private Button createNavButton(String text) {

        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.getStyleClass().add("nav-button");

        return btn;
    }

    // =========================
    // Screen Switching
    // =========================

    private void showHome() {

        contentArea.getChildren().setAll(
                new HomeScreen(cachedResults)
        );
    }

    private void showTraffic() {

        contentArea.getChildren().setAll(
                new TrafficViewerScreen()
        );
    }

    private void showFeatureInspector() {

        contentArea.getChildren().setAll(
                new FeatureInspectorScreen()
        );
    }

    private void showResults() {

        if (cachedResults == null) {

            System.out.println("Running detection pipeline...");

            PipelineRunner runner = new PipelineRunner();
            cachedResults = runner.run("data/nsl_kdd_test_clean.csv");

            System.out.println("Detection pipeline completed.");
        }

        contentArea.getChildren().setAll(
                new DetectionResultsScreen(cachedResults)
        );
    }

    private void showVisualization() {

        if (cachedResults == null) {

            System.out.println("Running detection pipeline for visualization...");

            PipelineRunner runner = new PipelineRunner();
            cachedResults = runner.run("data/nsl_kdd_test_clean.csv");
        }

        contentArea.getChildren().setAll(
                new VisualizationScreen(cachedResults)
        );
    }

    private void showAlerts() {

        contentArea.getChildren().setAll(
                new AlertsLogsScreen()
        );
    }
}
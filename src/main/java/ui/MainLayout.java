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

    private Button homeBtn;
    private Button trafficBtn;
    private Button featureBtn;
    private Button resultsBtn;
    private Button visualizationBtn;
    private Button alertsBtn;

    public MainLayout() {

        getStyleClass().add("root");

        System.out.println("Initializing NIDS detection pipeline...");
        PipelineRunner runner = new PipelineRunner();
        cachedResults = runner.run("data/nsl_kdd_test_clean.csv");
        System.out.println("Pipeline initialization complete.");

        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(200);
        sidebar.getStyleClass().add("sidebar");

        homeBtn = createNavButton("🏠  Home");
        trafficBtn = createNavButton("📡  Traffic Viewer");
        featureBtn = createNavButton("🧩  Feature Inspector");
        resultsBtn = createNavButton("🤖  Detection Results");
        visualizationBtn = createNavButton("📊  Visualization");
        alertsBtn = createNavButton("🚨  Alerts / Logs");

        sidebar.getChildren().addAll(
                homeBtn,
                trafficBtn,
                featureBtn,
                resultsBtn,
                visualizationBtn,
                alertsBtn
        );

        contentArea.getStyleClass().add("content-area");
        contentArea.setPadding(new Insets(20));

        setLeft(sidebar);
        setCenter(contentArea);

        showHome();

        homeBtn.setOnAction(e -> showHome());
        trafficBtn.setOnAction(e -> showTraffic());
        featureBtn.setOnAction(e -> showFeatureInspector());
        resultsBtn.setOnAction(e -> showResults());
        visualizationBtn.setOnAction(e -> showVisualization());
        alertsBtn.setOnAction(e -> showAlerts());
    }

    private Button createNavButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.getStyleClass().add("nav-button");
        return btn;
    }

    private void setActiveButton(Button activeButton) {
        homeBtn.getStyleClass().remove("active-nav");
        trafficBtn.getStyleClass().remove("active-nav");
        featureBtn.getStyleClass().remove("active-nav");
        resultsBtn.getStyleClass().remove("active-nav");
        visualizationBtn.getStyleClass().remove("active-nav");
        alertsBtn.getStyleClass().remove("active-nav");

        activeButton.getStyleClass().add("active-nav");
    }

    private void showHome() {
        setActiveButton(homeBtn);
        contentArea.getChildren().setAll(
                new HomeScreen(cachedResults)
        );
    }

    private void showTraffic() {
        setActiveButton(trafficBtn);
        contentArea.getChildren().setAll(
                new TrafficViewerScreen()
        );
    }

    private void showFeatureInspector() {
        setActiveButton(featureBtn);
        contentArea.getChildren().setAll(
                new FeatureInspectorScreen()
        );
    }

    private void showResults() {

        if (cachedResults == null) {
            PipelineRunner runner = new PipelineRunner();
            cachedResults = runner.run("data/nsl_kdd_test_clean.csv");
        }

        setActiveButton(resultsBtn);
        contentArea.getChildren().setAll(
                new DetectionResultsScreen(cachedResults)
        );
    }

    private void showVisualization() {

        if (cachedResults == null) {
            PipelineRunner runner = new PipelineRunner();
            cachedResults = runner.run("data/nsl_kdd_test_clean.csv");
        }

        setActiveButton(visualizationBtn);
        contentArea.getChildren().setAll(
                new VisualizationScreen(cachedResults)
        );
    }

    private void showAlerts() {
        setActiveButton(alertsBtn);
        contentArea.getChildren().setAll(
                new AlertsLogsScreen()
        );
    }
}
package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class HomeScreen extends BorderPane {

    public HomeScreen() {

        VBox container = new VBox(15);
        container.setPadding(new Insets(40));

        Label title = new Label("Network Intrusion Detection System");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        Label subtitle = new Label(
                "Dashboard for monitoring network traffic and intrusion detection."
        );

        Label info = new Label(
                "Use the sidebar to navigate between traffic viewer, features, detection results and alerts."
        );

        container.getChildren().addAll(title, subtitle, info);

        setCenter(container);
    }
}
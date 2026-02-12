// Traffic viewer screen for the NIDS GUI
package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class TrafficViewerScreen extends StackPane {

    public TrafficViewerScreen() {

        Label title = new Label("Traffic Viewer");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        VBox card = new VBox(title);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(350, 220);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: black;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;"
        );

        getChildren().add(card);
    }
}

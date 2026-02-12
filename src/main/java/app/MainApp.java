
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.MainLayout;

public class MainApp extends Application {

	@Override
	public void start(Stage stage) {
		MainLayout root = new MainLayout();
		Scene scene = new Scene(root, 1000, 600);

		stage.setTitle("AI-Powered NIDS");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}

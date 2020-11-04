package battleship.client;

import battleship.client.login.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientMain extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		VBox vbox = loader.load(getClass().getClassLoader().getResourceAsStream("login.fxml"));
		LoginController controller = loader.getController();

		Scene scene = new Scene(vbox);
		primaryStage.setScene(scene);


		primaryStage.show();
	}
}

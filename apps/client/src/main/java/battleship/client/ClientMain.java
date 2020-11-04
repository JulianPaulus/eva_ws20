package battleship.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
	/*	primaryStage.setTitle("Hello World!");
		Button btn = new Button();
		btn.setText("Say 'Hello World'");
		btn.setOnAction(event -> System.out.println("Hello World!"));

		StackPane root = new StackPane();
		root.getChildren().add(btn);
		primaryStage.setScene(new Scene(root, 300, 250));
		primaryStage.show();
*/
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/LobbyListScreen.fxml"));
		primaryStage.setTitle("LobbyTest");
		primaryStage.setScene(new Scene(root, 640, 400));
		primaryStage.show();
	}
}

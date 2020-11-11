package battleship.client;

import battleship.net.connection.Connection;
import battleship.net.connection.Constants;
import battleship.net.packet.LoginPacket;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.Socket;

public class Client extends Application {

	private static Client instance;

	private Connection connection;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		instance = this;
		Socket socket = new Socket("localhost", Constants.DEFAULT_PORT);
		connection = new Connection(socket);

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/LobbyListScreen.fxml"));
		primaryStage.setScene(new Scene(root));
		primaryStage.show();

		connection.writePacket(new LoginPacket("test123", "123456"));
		//battleship.net.connection.close();
		//System.exit(0);
	}

	public static Client getInstance() {
		return instance;
	}

	public Connection getConnection() {
		return connection;
	}
}

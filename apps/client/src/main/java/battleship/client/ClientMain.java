package battleship.client;

import battleship.client.lobby.LobbyListController;
import battleship.net.packet.AbstractPacket;
import battleship.net.packet.LobbyListPacket;
import battleship.net.packet.LoginPacket;
import battleship.util.Connection;
import battleship.util.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.Socket;

public class ClientMain extends Application {

	private static ClientMain instance;

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

		AbstractPacket packet = connection.readPacketBlocking();
		if (packet instanceof LobbyListPacket) {
			LobbyListController controller = LobbyListController.getNewestInstance();
			if(controller != null) {
				((LobbyListPacket) packet).act(controller, connection);
			}
		}

		//connection.close();
		//System.exit(0);
	}

	public static ClientMain getInstance() {
		return instance;
	}

	public Connection getConnection() {
		return connection;
	}
}

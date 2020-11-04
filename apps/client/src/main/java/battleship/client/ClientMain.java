package battleship.client;

import battleship.net.packet.TestPacket;
import battleship.util.Connection;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.Socket;

public class ClientMain extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Socket socket = new Socket("localhost", 1234);
		Connection connection = new Connection(socket);

		connection.writePacket(new TestPacket());
	}
}

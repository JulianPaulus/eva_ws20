package battleship.client;

import battleship.net.packet.TestPacket;
import battleship.util.Connection;
import battleship.util.Constants;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ClientMain extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Socket socket = new Socket("localhost", Constants.DEFAULT_PORT);
		Connection connection = new Connection(socket);

		connection.writePacket(new TestPacket());

		TimeUnit.SECONDS.sleep(1);
		connection.close();
		System.exit(0);
	}
}

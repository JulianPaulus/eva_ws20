package battleship.client;

import battleship.net.packet.AbstractPacket;
import battleship.net.packet.TestPacket;
import battleship.util.Connection;
import battleship.util.Constants;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.Socket;
import java.util.concurrent.TimeUnit;

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

		connection.writePacket(new TestPacket());


		TimeUnit.SECONDS.sleep(1);

		AbstractPacket packet = connection.readPacketBlocking();
		if (packet instanceof TestPacket) {
			System.out.println(((TestPacket) packet).getTimestamp());
		}

		connection.close();
		System.exit(0);
	}

	public static ClientMain getInstance() {
		return instance;
	}

	public Connection getConnection() {
		return connection;
	}
}

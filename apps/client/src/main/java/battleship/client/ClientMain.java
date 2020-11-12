package battleship.client;

import battleship.client.packet.receive.LobbyListPacket;
import battleship.client.packet.receive.LoginResponsePacket;
import battleship.client.packet.receive.factory.LobbyListPacketFactory;
import battleship.client.packet.receive.factory.LoginResponseFactory;
import battleship.net.connection.Connection;
import battleship.net.connection.PacketReader;
import battleship.net.factory.AbstractPacketFactory;
import battleship.net.factory.TestPacketFactory;
import battleship.net.packet.TestPacket;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientMain extends Application {

	private static final Logger logger = LoggerFactory.getLogger(ClientMain.class);

	private static ClientMain instance;
	private Stage stage;

	private Connection connection;

	static {
		Map<Byte, AbstractPacketFactory<?>> packetFactoryMap = new HashMap<>();
		packetFactoryMap.put(TestPacket.IDENTIFIER, new TestPacketFactory());
		packetFactoryMap.put(LobbyListPacket.IDENTIFIER, new LobbyListPacketFactory());
		packetFactoryMap.put(LoginResponsePacket.IDENTIFIER, new LoginResponseFactory());
		PacketReader.setFactoryMap(packetFactoryMap);
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		instance = this;
		this.stage = primaryStage;

		stage.setTitle("Schiffe Versenken - EVA WS20/21");

		FXMLLoader loader = new FXMLLoader();
		loader.load(getClass().getResourceAsStream("/login.fxml"));
		stage.setScene(new Scene(loader.getRoot()));
		stage.show();
	}

	public Connection connect(final String address, final int port) throws IOException {
		if (connection == null) {
			Socket socket = new Socket(address, port);
			connection = new Connection(socket);

			return connection;
		}

		logger.warn("client tried establishing a connection while a connection was present");
		return connection;
	}

	public static ClientMain getInstance() {
		return instance;
	}

	public Connection getConnection() {
		return connection;
	}

	public Stage getStage() {
		return this.stage;
	}
}

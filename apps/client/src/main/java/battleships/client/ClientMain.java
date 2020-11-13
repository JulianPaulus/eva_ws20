package battleships.client;

import battleships.client.packet.receive.LobbyListPacket;
import battleships.client.packet.receive.LoginResponsePacket;
import battleships.client.packet.receive.factory.LobbyListPacketFactory;
import battleships.client.packet.receive.factory.LoginResponseFactory;
import battleships.net.connection.Connection;
import battleships.net.connection.PacketReader;
import battleships.net.factory.AbstractPacketFactory;
import battleships.net.factory.TestPacketFactory;
import battleships.net.packet.TestPacket;
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
		Scene scene = new Scene(loader.getRoot());
		scene.getStylesheets().add(getClass().getResource("/style.css").toString());
		stage.setScene(scene);
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

	@Override
	public void stop() throws Exception {
		super.stop();

		if (connection != null) {
			connection.close();
		}
	}
}

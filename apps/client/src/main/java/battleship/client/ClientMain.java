package battleship.client;

import battleship.client.packet.receive.LobbyListPacket;
import battleship.client.packet.receive.LoginResponsePacket;
import battleship.client.packet.receive.factory.LobbyListPacketFactory;
import battleship.client.packet.receive.factory.LoginResponseFactory;
import battleship.client.packet.send.LoginPacket;
import battleship.net.connection.Connection;
import battleship.net.connection.Constants;
import battleship.net.connection.PacketReader;
import battleship.net.factory.AbstractPacketFactory;
import battleship.net.factory.TestPacketFactory;
import battleship.net.packet.TestPacket;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientMain extends Application {

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
		Socket socket = new Socket("localhost", Constants.DEFAULT_PORT);
		connection = new Connection(socket);

		//connection.writePacket(new TestPacket());
		connection.writePacket(new LoginPacket("test123", "123456"));
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

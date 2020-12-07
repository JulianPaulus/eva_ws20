package battleship.client;

import battleships.client.GameWindow.GameWindow;
import battleships.client.packet.receive.LobbyListPacket;
import battleships.client.packet.receive.LoginResponsePacket;
import battleships.client.packet.receive.factory.LobbyListPacketFactory;
import battleships.client.packet.receive.factory.LoginResponseFactory;
import battleships.net.connection.Connection;
//import battleships.net.connection.Constants;
import battleships.net.connection.PacketReader;
import battleships.net.factory.AbstractPacketFactory;
//import battleships.net.factory.TestPacketFactory;
//import battleships.net.packet.TestPacket;
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
		//packetFactoryMap.put(TestPacket.IDENTIFIER, new TestPacketFactory());
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

		GameWindow window=new GameWindow(primaryStage);

		primaryStage.show();

		//Socket socket = new Socket("localhost", Constants.DEFAULT_PORT);
		//connection = new Connection(socket);

		//connection.writePacket(new TestPacket());
		//connection.writePacket(new LoginPacket("test123", "123456"));
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

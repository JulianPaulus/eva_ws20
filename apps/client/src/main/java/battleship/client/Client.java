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
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Client extends Application {

	private static Client instance;

	private Connection connection;

	static {
		Map<Byte, AbstractPacketFactory<?>> packetFactoryMap = new HashMap<>();
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
		Socket socket = new Socket("localhost", Constants.DEFAULT_PORT);
		connection = new Connection(socket);

		connection.writePacket(new LoginPacket("test123", "123456"));
		/*
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/LobbyListScreen.fxml"));
		primaryStage.setScene(new Scene(root));
		primaryStage.show();*/
	}

	public static Client getInstance() {
		return instance;
	}

	public Connection getConnection() {
		return connection;
	}
}

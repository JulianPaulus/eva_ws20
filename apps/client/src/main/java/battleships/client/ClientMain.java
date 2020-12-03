package battleships.client;

import battleships.client.packet.receive.GameJoinedPacket;
import battleships.client.packet.receive.LobbyListPacket;
import battleships.client.packet.receive.LoginResponsePacket;
import battleships.client.packet.receive.RegistrationErrorResponsePacket;
import battleships.client.packet.receive.ServerErrorPacket;
import battleships.client.packet.receive.factory.GameJoinedPacketFactory;
import battleships.client.packet.receive.factory.LobbyListPacketFactory;
import battleships.client.packet.receive.factory.LoginResponseFactory;
import battleships.client.packet.receive.factory.RegistrationErrorResponseFactory;
import battleships.client.packet.receive.factory.ServerErrorPacketFactory;
import battleships.net.connection.Connection;
import battleships.net.connection.ConnectionEvent;
import battleships.net.connection.PacketReader;
import battleships.net.factory.AbstractPacketFactory;
import battleships.observable.Observable;
import battleships.observable.Observer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientMain extends Application implements Observer<ConnectionEvent> {

	private static final Logger logger = LoggerFactory.getLogger(ClientMain.class);

	private static ClientMain instance;
	private Stage stage;

	private Connection connection;

	static {
		Map<Byte, AbstractPacketFactory<?>> packetFactoryMap = new HashMap<>();
		packetFactoryMap.put(LobbyListPacket.IDENTIFIER, new LobbyListPacketFactory());
		packetFactoryMap.put(LoginResponsePacket.IDENTIFIER, new LoginResponseFactory());
		packetFactoryMap.put(RegistrationErrorResponsePacket.IDENTIFIER, new RegistrationErrorResponseFactory());
		packetFactoryMap.put(GameJoinedPacket.IDENTIFIER, new GameJoinedPacketFactory());
		packetFactoryMap.put(ServerErrorPacket.IDENTIFIER, new ServerErrorPacketFactory());
		PacketReader.setFactoryMap(packetFactoryMap);
	}

	public static void main(final String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
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
			connection.addObserver(instance);

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

	@Override
	public void update(final Observable<ConnectionEvent> o, final ConnectionEvent event) {
		if (event == ConnectionEvent.DISCONNECTED) {
			Platform.runLater(() -> {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.initModality(Modality.APPLICATION_MODAL);
				alert.initOwner(getStage());
				alert.setHeaderText("Verbindung zum Server verloren!");
				alert.setContentText("Das Programm wird geschlossen.");
				alert.showAndWait();
				System.exit(1);
			});
		}
	}
}

package battleships.client;

import battleships.client.login.LoginController;
import battleships.client.packet.receive.*;
import battleships.client.packet.receive.factory.*;
import battleships.client.util.ClientState;
import battleships.net.connection.Connection;
import battleships.net.connection.ConnectionEvent;
import battleships.net.connection.PacketReader;
import battleships.net.factory.AbstractPacketFactory;
import battleships.net.factory.StateLessPacketFactory;
import battleships.observable.Observable;
import battleships.observable.Observer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientMain extends Application implements Observer<ConnectionEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientMain.class);

	private static ClientMain instance;
	private Stage stage;
	private ClientState state;
	private final Lock stateLock = new ReentrantLock();

	private Connection connection;

	static {
		Map<Byte, AbstractPacketFactory<?>> packetFactoryMap = new HashMap<>();
		packetFactoryMap.put(LobbyListPacket.IDENTIFIER, new LobbyListPacketFactory());
		packetFactoryMap.put(LoginResponsePacket.IDENTIFIER, new LoginResponseFactory());
		packetFactoryMap.put(RegistrationErrorResponsePacket.IDENTIFIER, new RegistrationErrorResponseFactory());
		packetFactoryMap.put(GameJoinedPacket.IDENTIFIER, new GameJoinedPacketFactory());
		packetFactoryMap.put(ServerErrorPacket.IDENTIFIER, new ServerErrorPacketFactory());
		packetFactoryMap.put(GamePlayerDoSetupPacket.IDENTIFIER, new GameStartPacketFactory());
		packetFactoryMap.put(ChatMessagePacket.IDENTIFIER, new ChatMessagePacketFactory());
		packetFactoryMap.put(GameOtherPlayerSetupPacket.IDENTIFIER, new StateLessPacketFactory<>(GameOtherPlayerSetupPacket.class));
		packetFactoryMap.put(IllegalShipPositionPacket.IDENTIFIER, new StateLessPacketFactory<>(IllegalShipPositionPacket.class));
		packetFactoryMap.put(GamePlayersTurnPacket.IDENTIFIER, new StateLessPacketFactory<>(GamePlayersTurnPacket.class));
		packetFactoryMap.put(GameEnemiesTurnPacket.IDENTIFIER, new StateLessPacketFactory<>(GameEnemiesTurnPacket.class));
		packetFactoryMap.put(GameShootResponsePacket.IDENTIFIER, new GameShootResponsePacketFactory());
		PacketReader.setFactoryMap(packetFactoryMap);
	}

	public static void main(final String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		instance = this;
		this.stage = primaryStage;
		this.state = ClientState.DISCONNECTED;

		stage.setResizable(false);
		stage.setTitle("Schiffe Versenken - EVA WS20/21");
		LoginController.openWindow(stage);
	}

	public Connection connect(final String address, final int port) throws IOException {
		if (connection == null) {
			Socket socket = new Socket(address, port);
			connection = new Connection(socket);
			connection.addObserver(instance);
			setState(ClientState.CONNECTED);

			return connection;
		}

		LOGGER.warn("client tried establishing a connection while a connection was present");
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

	public ClientState getState() {
		try {
			stateLock.lock();
			return state;
		} finally {
			stateLock.unlock();
		}
	}

	public void setState(final ClientState newState) {
		try {
			stateLock.lock();
			state = newState;
		} finally {
			stateLock.unlock();
		}
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		setState(ClientState.SHUTTING_DOWN);

		if (connection != null) {
			connection.close();
		}
	}

	@Override
	public void update(final Observable<ConnectionEvent> o, final ConnectionEvent event) {
		if (event == ConnectionEvent.DISCONNECTED) {
			this.connection = null;
			if (getState() == ClientState.SHUTTING_DOWN) return;
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

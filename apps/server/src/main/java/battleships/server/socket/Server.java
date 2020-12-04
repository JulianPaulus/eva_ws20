package battleships.server.socket;

import battleships.net.connection.Connection;
import battleships.server.service.ConnectionService;
import battleships.server.util.ConnectionManager;
import battleships.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

	private static final Server INSTANCE = new Server();
	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

	private final ConnectionService connectionService = ConnectionService.getInstance();
	private final ConnectionManager connectionManager;
	private ServerSocket serverSocket;

	private Server()  {
		super("server");

		try {
			this.serverSocket = new ServerSocket(Constants.Server.DEFAULT_PORT);
		} catch (final IOException e) {
			LOGGER.error("critical error during startup", e);
			System.exit(1);
		}

		this.connectionManager = new ConnectionManager();

		this.connectionManager.start();
	}

	@Override
	public void run() {
		LOGGER.info("Server starting...");
		while (!serverSocket.isClosed() && !isInterrupted()) {
			try {
				Socket socket = serverSocket.accept();
				LOGGER.info("new connection from {}", socket.getInetAddress().getHostAddress());
				Connection newConnection = new Connection(socket);
				newConnection.addObserver(connectionService);
				connectionService.registerConnection(newConnection);
			} catch (final IOException e) {
				LOGGER.trace("error in serversocket loop", e);
			}
		}
	}

	public static Server getInstance() throws IOException {
		return INSTANCE;
	}
}

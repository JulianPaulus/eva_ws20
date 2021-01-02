package battleships.server.socket;

import battleships.net.connection.Connection;
import battleships.server.service.ConnectionService;
import battleships.server.util.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

	private static Server INSTANCE;
	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

	private final ConnectionService connectionService = ConnectionService.getInstance();
	private final ConnectionManager connectionManager;
	private ServerSocket serverSocket;

	public Server()  {
		super("server");
		INSTANCE = this;

		try {
			this.serverSocket = new ServerSocket(ServerConfig.getInstance().getPort());
		} catch (final IOException e) {
			LOGGER.error("critical error during startup", e);
			System.exit(1);
		}

		this.connectionManager = new ConnectionManager();
		this.connectionManager.start();
	}

	@Override
	public void run() {
		LOGGER.info("Server starting on port {}...", serverSocket.getLocalPort());
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
		connectionManager.interrupt();
	}

	public static Server getInstance() throws IOException {
		return INSTANCE;
	}
}

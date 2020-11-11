package battleship.server.socket;

import battleship.net.connection.Connection;
import battleship.net.connection.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(Server.class);

	private static Server instance;

	private final ServerSocket serverSocket;

	private Server() throws IOException {
		super("server");
		this.serverSocket = new ServerSocket(Constants.DEFAULT_PORT);
	}

	@Override
	public void run() {
		logger.info("Server starting...");
		while (!serverSocket.isClosed() && !isInterrupted()) {
			try {
				Socket socket = serverSocket.accept();
				logger.info("new connection from {}", socket.getInetAddress().getHostAddress());
				Connection newConnection = new Connection(socket);
			} catch (IOException e) {
				logger.trace("error in serversocket loop", e);
			}
		}
	}

	public synchronized static Server getInstance() throws IOException {
		if (instance == null) {
			instance = new Server();
		}
		return instance;
	}
}

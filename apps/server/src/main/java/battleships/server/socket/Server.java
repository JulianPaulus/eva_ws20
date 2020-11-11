package battleships.server.socket;

import battleship.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import refactor.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(Server.class);

	private static Server instance;

	private final ServerSocket serverSocket;
	private final LobbyThread lobbyThread;

	private Server() throws IOException {
		super("server");
		this.serverSocket = new ServerSocket(Constants.DEFAULT_PORT);
		this.lobbyThread = LobbyThread.getInstance();
	}

	@Override
	public void run() {
		logger.info("Server starting...");
		lobbyThread.start();
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

	@Override
	public void interrupt() {
		super.interrupt();
		lobbyThread.interrupt();
	}
}

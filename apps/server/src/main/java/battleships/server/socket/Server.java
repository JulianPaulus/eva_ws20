package battleships.server.socket;

import battleship.net.packet.AbstractPacket;
import battleship.net.packet.TestPacket;
import battleship.util.Connection;
import battleship.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(Server.class);

	private static Server instance;

	private final ServerSocket serverSocket;
	private final List<Connection> connections;

	private Server() throws IOException {
		this.serverSocket = new ServerSocket(Constants.DEFAULT_PORT);
		this.connections = new ArrayList<>();

		setName("server");

		logger.info("Server starting...");
	}

	@Override
	public void run() {
		while (!serverSocket.isClosed() && !isInterrupted()) {
			try {
				Socket socket = serverSocket.accept();
				logger.info("new connection from {}", socket.getInetAddress().getHostAddress());
				Connection newConnection = new Connection(socket);
				connections.add(newConnection);

				AbstractPacket packet = newConnection.readPacketBlocking();
				if (packet instanceof TestPacket) {
					logger.info(String.valueOf(((TestPacket) packet).getTimestamp()));
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static Server getInstance() throws IOException {
		if (instance == null) {
			instance = new Server();
		}
		return instance;
	}

}

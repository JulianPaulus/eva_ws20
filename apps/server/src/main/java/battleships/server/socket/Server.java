package battleships.server.socket;

import battleship.net.packet.AbstractPacket;
import battleship.net.packet.TestPacket;
import battleship.util.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {

	private final ServerSocket serverSocket;
	private final List<Connection> connections;

	public Server() throws IOException {
		this.serverSocket = new ServerSocket(1234);
		this.connections = new ArrayList<>();
	}

	@Override
	public void run() {
		while (!serverSocket.isClosed() && !isInterrupted()) {
			try {
				Socket socket = serverSocket.accept();
				Connection newConnection = new Connection(socket);
				connections.add(newConnection);

				AbstractPacket packet = newConnection.readPacketBlocking();
				if (packet instanceof TestPacket) {
					System.out.println(((TestPacket) packet).getTimestamp());
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

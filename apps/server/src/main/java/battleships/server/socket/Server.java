package battleships.server.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

	private final ServerSocket serverSocket;

	public Server() throws IOException {
		this.serverSocket = new ServerSocket(1234);
	}

	@Override
	public void run() {
		while (!serverSocket.isClosed() && !isInterrupted()) {
			try {
				Socket socket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

package battleships.server;

import battleships.server.socket.Server;

import java.io.IOException;

public class ServerMain {


	public static void main(String... args) throws IOException {
		Server server = new Server();

		server.start();
	}

}

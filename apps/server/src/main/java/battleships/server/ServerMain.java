package battleships.server;

import battleship.server.socket.Server;

import java.io.IOException;

public class ServerMain {


	public static void main(String... args) throws IOException {
		Server server = Server.getInstance();

		server.start();
	}

}

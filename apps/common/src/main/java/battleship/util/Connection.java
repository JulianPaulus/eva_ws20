package battleship.util;

import java.io.IOException;
import java.net.Socket;

public class Connection {

	private final Socket socket;
	private final PacketReader reader;
	private final PacketWriter writer;

	public Connection(Socket socket) throws IOException {
		this.socket = socket;

		this.reader = new PacketReader(null);
		this.writer = new PacketWriter(socket.getOutputStream());
	}

}

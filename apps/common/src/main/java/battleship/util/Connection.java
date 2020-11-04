package battleship.util;

import battleship.net.packet.AbstractPacket;

import java.io.IOException;
import java.net.Socket;

public class Connection {

	protected final Socket socket;
	protected final PacketReader reader;
	protected final PacketWriter writer;

	public Connection(final Socket socket) throws IOException {
		this.socket = socket;

		this.reader = new PacketReader(socket.getInputStream());
		this.writer = new PacketWriter(socket.getOutputStream());

		reader.start();
		writer.start();
	}

	public AbstractPacket readPacket() throws InterruptedException {
		return reader.read();
	}

	public AbstractPacket readPacketBlocking() throws InterruptedException {
		return reader.readBlocking();
	}

	public void writePacket(AbstractPacket packet) {
		writer.write(packet);
	}

}

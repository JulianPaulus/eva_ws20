package battleship.util;

import battleship.net.packet.AbstractPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class Connection {

	private static final Logger logger = LoggerFactory.getLogger(Connection.class);

	protected final Socket socket;
	protected final PacketReader reader;
	protected final PacketWriter writer;

	protected boolean closed = false;

	public Connection(final Socket socket) throws IOException {
		this.socket = socket;

		this.reader = new PacketReader(socket.getInputStream(), this);
		this.writer = new PacketWriter(socket.getOutputStream(), this);

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

	public synchronized void close() throws IOException {
		if (!closed) {
			writer.close();
			reader.close();
			socket.close();

			logger.debug("closing connection with {}", socket.getInetAddress().getHostAddress());
			closed = true;
		}
	}

}

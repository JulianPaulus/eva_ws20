package refactor;

import battleship.net.packet.AbstractPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import refactor.packethandler.AbstractPacketHandler;
import refactor.packethandler.PreAuthPacketHandler;

import java.io.IOException;
import java.net.Socket;

public class Connection {

	private static final Logger logger = LoggerFactory.getLogger(Connection.class);

	protected final Socket socket;
	protected final PacketReader reader;
	protected final PacketWriter writer;
	protected final AbstractPacketHandler abstractPacketHandler;

	protected boolean closed = false;

	public Connection(final Socket socket) throws IOException {
		this.socket = socket;
		this.reader = new PacketReader(socket.getInputStream(), this);
		this.writer = new PacketWriter(socket.getOutputStream(), this);
		this.abstractPacketHandler = new PreAuthPacketHandler();
		reader.start();
	}

	protected Connection(Connection connection) {
		this.socket = connection.socket;
		this.reader = connection.reader;
		this.writer = connection.writer;
		this.abstractPacketHandler = connection.abstractPacketHandler;
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

	public AbstractPacketHandler getPacketHandler() {
		return this.abstractPacketHandler;
	}

	public boolean isAuthenticated() {
		return false;
	}

	@Override
	public String toString() {
		return "Connection{" +
			"socket=" + socket +
			", reader=" + reader +
			", writer=" + writer +
			", closed=" + closed +
			'}';
	}
}

package battleships.net.connection;

import battleships.net.connection.packethandler.AbstractPacketHandler;
import battleships.net.connection.packethandler.PreAuthPacketHandler;
import battleships.net.packet.SendPacket;
import battleships.observable.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class Connection extends Observable<ConnectionEvent> {

	private static final Logger logger = LoggerFactory.getLogger(Connection.class);

	protected final Socket socket;
	protected final PacketReader reader;
	protected final PacketWriter writer;
	protected AbstractPacketHandler<?, ?> packetHandler;

	protected boolean closed = false;

	public Connection(final Socket socket) throws IOException {
		this.socket = socket;
		this.reader = new PacketReader(socket.getInputStream(), this);
		this.writer = new PacketWriter(socket.getOutputStream());
		this.packetHandler = new PreAuthPacketHandler();
		reader.start();

		logger.info("established connection with {}", socket.getInetAddress().getHostAddress());
	}

	protected Connection(final Connection connection) {
		this.socket = connection.socket;
		this.reader = connection.reader;
		this.writer = connection.writer;
		this.packetHandler = connection.packetHandler;
		this.closed = connection.closed;
	}

	public void writePacket(final SendPacket packet) {
		try {
			writer.write(packet);
		} catch (final IOException e) {
			logger.error("error while writing a packet to {}", this, e);
			try {
				close();
			} catch (final IOException ioException) {
				logger.error("error while closing connection {}", this, e);
			}
		}
	}

	public synchronized void close() throws IOException {
		if (!closed) {
			writer.close();
			reader.close();
			socket.close();

			logger.info("closing connection with {}", socket.getInetAddress().getHostAddress());
			closed = true;

			updateObservers(ConnectionEvent.DISCONNECTED);
		}
	}

	public AbstractPacketHandler<?, ?> getPacketHandler() {
		return this.packetHandler;
	}

	public PacketReader getPacketReader() {
		return reader;
	}

	@Override
	public String toString() {
		return "Connection{" +
			"socket=" + socket +
			", reader=" + reader +
			", writer=" + writer +
			", packetHandler=" + packetHandler +
			", closed=" + closed +
			'}';
	}
}

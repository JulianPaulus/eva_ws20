package battleships.net.connection;

import battleships.net.connection.packethandler.AbstractPacketHandler;
import battleships.net.connection.packethandler.PreAuthPacketHandler;
import battleships.net.packet.SendPacket;
import battleships.observable.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class Connection extends Observable<ConnectionEvent> {

	private static final Logger logger = LoggerFactory.getLogger(Connection.class);

	protected final Socket socket;
	protected final PacketReader reader;
	protected final PacketWriter writer;
	protected final UUID uuid;

	protected boolean closed = false;
	protected AbstractPacketHandler<?, ?> packetHandler;

	private AtomicLong lastInteraction;

	public Connection(final Socket socket) throws IOException {
		this.socket = socket;
		this.reader = new PacketReader(socket.getInputStream(), this);
		this.writer = new PacketWriter(socket.getOutputStream());
		this.packetHandler = new PreAuthPacketHandler();
		this.uuid = UUID.randomUUID();
		this.lastInteraction = new AtomicLong(System.currentTimeMillis());
		reader.start();

		logger.info("established connection with {}", socket.getInetAddress().getHostAddress());
	}

	protected Connection(final Connection connection) {
		this.socket = connection.socket;
		this.reader = connection.reader;
		this.writer = connection.writer;
		this.packetHandler = connection.packetHandler;
		this.closed = connection.closed;
		this.uuid = connection.uuid;
		this.lastInteraction = connection.lastInteraction;
	}

	public void writePacket(final SendPacket packet) {
		try {
			writer.write(packet);
			updateInteractionTime();
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

	public UUID getUUID() {
		return uuid;
	}

	public void updateInteractionTime() {
		lastInteraction.set(System.currentTimeMillis());
	}

	@Override
	public String toString() {
		return "Connection{" +
			"socket=" + socket +
			", reader=" + reader +
			", writer=" + writer +
			", uuid=" + uuid +
			", closed=" + closed +
			", packetHandler=" + packetHandler +
			'}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Connection that = (Connection) o;
		return closed == that.closed && socket.equals(that.socket) && reader.equals(that.reader) && writer
			.equals(that.writer) && uuid.equals(that.uuid) && packetHandler
			.equals(that.packetHandler) && lastInteraction
			.equals(that.lastInteraction);
	}

	@Override
	public int hashCode() {
		return Objects.hash(socket, reader, writer, uuid, closed, packetHandler, lastInteraction);
	}
}

package battleships.net.connection;

import battleships.net.connection.packethandler.AbstractPacketHandler;
import battleships.net.connection.packethandler.PreAuthPacketHandler;
import battleships.net.packet.SendPacket;
import battleships.observable.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class Connection extends Observable<ConnectionEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Connection.class);

	protected final Socket socket;
	protected final PacketReader reader;
	protected final PacketWriter writer;
	protected final UUID uuid;

	protected boolean closed = false;
	protected AbstractPacketHandler<?, ?> packetHandler;
	protected long lastHeartbeat = System.currentTimeMillis();

	private final AtomicLong lastInteractionMS;

	public Connection(final Socket socket) throws IOException {
		this.socket = socket;
		this.reader = new PacketReader(socket.getInputStream(), this);
		this.writer = new PacketWriter(socket.getOutputStream());
		this.packetHandler = new PreAuthPacketHandler();
		this.uuid = UUID.randomUUID();
		this.lastInteractionMS = new AtomicLong(System.currentTimeMillis());
		reader.start();

		LOGGER.info("established connection with {}", socket.getInetAddress().getHostAddress());
	}

	protected Connection(final Connection connection) {
		this.socket = connection.socket;
		this.reader = connection.reader;
		this.writer = connection.writer;
		this.packetHandler = connection.packetHandler;
		this.closed = connection.closed;
		this.uuid = connection.uuid;
		this.lastInteractionMS = connection.lastInteractionMS;
		this.observers = connection.observers;
	}

	public void writePacket(final SendPacket packet) {
		try {
			writer.write(packet);
			updateInteractionTime();
		} catch (final IOException e) {
			LOGGER.error("error while writing a packet to {}", this, e);
			close();
		}
	}

	public synchronized void close() {
		if (!closed) {
			try {
				writer.close();
				reader.close();
				socket.close();

				LOGGER.info("closing connection with {}", socket.getInetAddress().getHostAddress());
				closed = true;

				updateObservers(ConnectionEvent.DISCONNECTED);
			} catch (final IOException e) {
				LOGGER.warn("error while closing connection", e);
			}
		}
	}

	public synchronized boolean isClosed() {
		return closed;
	}

	public synchronized void updateHeartbeat() {
		this.lastHeartbeat = System.currentTimeMillis();
	}

	public synchronized long getLastHeartbeat() {
		return lastHeartbeat;
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
		lastInteractionMS.set(System.currentTimeMillis());
	}

	public long getLastInteractionMS() {
		return lastInteractionMS.get();
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
			.equals(that.packetHandler) && lastInteractionMS
			.equals(that.lastInteractionMS);
	}

	@Override
	public int hashCode() {
		return Objects.hash(socket, reader, writer, uuid, closed, packetHandler, lastInteractionMS);
	}
}

package battleships.net.connection;

import battleships.net.exception.IllegalPacketTypeException;
import battleships.net.factory.AbstractPacketFactory;
import battleships.net.packet.IReceivePacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketReader extends Thread {
	private static final Logger LOGGER = LoggerFactory.getLogger(PacketReader.class);
	private static final AtomicInteger OBJ_COUNT = new AtomicInteger(0);

	private final DataInputStream stream;
	private Connection connection;
	private static Map<Byte, AbstractPacketFactory<?>> factoryMap = new HashMap<>();

	public PacketReader(final InputStream stream, final Connection connection) {
		super("connection-" + OBJ_COUNT.incrementAndGet());
		this.stream = new DataInputStream(stream);
		this.connection = connection;
	}

	public static void setFactoryMap(final Map<Byte, AbstractPacketFactory<?>> factoryMap) {
		PacketReader.factoryMap = factoryMap;
	}

	@Override
	public void run() {
		while(!isInterrupted()) {
			try {
				byte identifier = stream.readByte();
				AbstractPacketFactory<?> packetFactory = factoryMap.get(identifier);
				LOGGER.debug("read packet identifier {}", identifier);
				if (packetFactory != null) {
					IReceivePacket<?> packet = packetFactory.unmarshal(stream);
					try {
						connection.getPacketHandler().handle(packet, connection);
					} catch (final IllegalPacketTypeException e) {
						LOGGER.warn("The received packet produced an error!: " + e.getMessage());
					}
				}
			} catch (final IOException e) {
				if (connection != null) {
					connection.close();
				}
				interrupt();
			}
		}
	}

	public void setConnection(final Connection connection) {
		this.connection = connection;
	}

	public void close() throws IOException {
		this.interrupt();
		stream.close();
		this.connection = null;
	}
}

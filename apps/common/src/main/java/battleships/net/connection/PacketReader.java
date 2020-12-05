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

public class PacketReader extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(PacketReader.class);

	private final DataInputStream stream;
	private Connection connection;
	private static Map<Byte, AbstractPacketFactory<?>> factoryMap = new HashMap<>();

	public PacketReader(final InputStream stream, final Connection connection) {
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
				if (packetFactory != null) {
					IReceivePacket<?> packet = packetFactory.unmarshal(stream);
					try {
						connection.getPacketHandler().handle(packet, connection);
					} catch (IllegalPacketTypeException e) {
						logger.warn("The received packet produced an error!: " + e.getMessage());
					}
				}
			} catch (IOException e) {
				try {
					if (connection != null) {
						connection.close();
					}
					interrupt();
				} catch (IOException ioException) {
					logger.error("error while closing connection", ioException);
				}
			}
		}
	}

	protected void setConnection(final Connection connection) {
		this.connection = connection;
	}

	public void close() throws IOException {
		this.interrupt();
		stream.close();
		this.connection = null;
	}
}

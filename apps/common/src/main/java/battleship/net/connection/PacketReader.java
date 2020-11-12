package battleship.net.connection;

import battleship.net.factory.AbstractPacketFactory;
import battleship.net.packet.AbstractPacket;
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

	public static void setFactoryMap(Map<Byte, AbstractPacketFactory<?>> factoryMap) {
		PacketReader.factoryMap = factoryMap;
	}

	@Override
	public void run() {
		while(!isInterrupted()) {
			try {
				byte identifier = stream.readByte();
				AbstractPacketFactory<?> packetFactory = factoryMap.get(identifier);
				if (packetFactory != null) {
					AbstractPacket packet = packetFactory.unmarshal(stream);
					connection.getPacketHandler().handle(packet, connection);
				}
			} catch (IOException e) {
				try {
					connection.close();
					interrupt();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
		}
	}

	protected void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void close() throws IOException {
		this.interrupt();
		stream.close();
	}
}

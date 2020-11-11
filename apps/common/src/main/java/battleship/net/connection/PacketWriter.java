package battleship.net.connection;

import battleship.net.packet.AbstractPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

public class PacketWriter {
	private static final Logger logger = LoggerFactory.getLogger(PacketWriter.class);

	private final OutputStream stream;
	private final Connection connection;

	public PacketWriter(final OutputStream stream, final Connection connection) {
		this.stream = stream;
		this.connection = connection;
	}

	public void write(final AbstractPacket<? extends Connection> packet) throws IOException {
		byte[] data = packet.marshal();
		stream.write(data);
		stream.flush();
	}

	public void close() throws IOException {
		stream.close();
	}
}

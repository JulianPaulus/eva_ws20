package battleships.net.connection;

import battleships.net.packet.SendPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

public class PacketWriter {
	private static final Logger logger = LoggerFactory.getLogger(PacketWriter.class);

	private final OutputStream stream;

	public PacketWriter(final OutputStream stream) {
		this.stream = stream;
	}

	public synchronized void write(final SendPacket packet) throws IOException {
		byte[] data = packet.marshal();
		stream.write(data);
		stream.flush();
	}

	public synchronized void close() throws IOException {
		stream.close();
	}
}

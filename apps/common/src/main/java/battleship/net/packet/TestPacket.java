package battleship.net.packet;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TestPacket extends AbstractPacket {

	public static final byte IDENTIFIER = 0x0;
	public static final int MARSHALLED_SIZE = 0x9;

	private long timestamp;

	public TestPacket() {
		this.timestamp = System.currentTimeMillis();
	}

	public TestPacket(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public byte[] marshal() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(MARSHALLED_SIZE);
		DataOutputStream dos = new DataOutputStream(bos);

		dos.writeByte(IDENTIFIER);
		dos.writeLong(timestamp);

		return bos.toByteArray();
	}

	public long getTimestamp() {
		return timestamp;
	}
}

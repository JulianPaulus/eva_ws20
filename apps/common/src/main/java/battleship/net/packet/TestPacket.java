package battleship.net.packet;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TestPacket extends AbstractPacket {

	public static final byte IDENTIFIER = 0x0;

	private long timestamp;

	public TestPacket() {
		this.timestamp = System.currentTimeMillis();
	}

	public TestPacket(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected DataOutputStream writeContent(DataOutputStream dos) throws IOException {
		dos.writeByte(IDENTIFIER);
		dos.writeLong(timestamp);
		return dos;
	}

	public long getTimestamp() {
		return timestamp;
	}
}

package battleship.net.packet;

import battleship.net.connection.Connection;

import java.io.DataOutputStream;
import java.io.IOException;

public class TestPacket extends SendPacket implements IPreAuthReceivePacket {

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

	@Override
	public void act(Connection connection) {
		System.out.println(timestamp);
		try {
			connection.writePacket(new TestPacket());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

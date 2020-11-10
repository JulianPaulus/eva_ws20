package battleship.net.factory;

import battleship.net.packet.TestPacket;

import java.io.DataInputStream;
import java.io.IOException;

public class TestPacketFactory extends AbstractPacketFactory<TestPacket> {
	@Override
	public TestPacket unmarshal(final DataInputStream stream) throws IOException {
		long timestamp = stream.readLong();

		return new TestPacket(timestamp);
	}
}

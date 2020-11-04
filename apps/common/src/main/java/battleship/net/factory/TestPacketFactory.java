package battleship.net.factory;

import battleship.net.packet.TestPacket;

import java.io.DataInputStream;

public class TestPacketFactory extends AbstractPacketFactory<TestPacket> {
	@Override
	public TestPacket unmarshall(final DataInputStream stream) {
		return null;
	}
}

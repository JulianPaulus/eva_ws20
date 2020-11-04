package battleship.net.factory;

import battleship.net.packet.AbstractPacket;

import java.io.DataInputStream;

public class TestPacketFactory extends AbstractPacketFactory {
	@Override
	public AbstractPacket unmarshall(final DataInputStream stream) {
		return null;
	}
}

package battleship.net.factory;

import battleship.net.packet.AbstractPacket;

import java.io.DataInputStream;

public class TestPacketFactory extends PacketFactory{
	@Override
	public AbstractPacket unmarshall(DataInputStream stream) {
		return null;
	}
}

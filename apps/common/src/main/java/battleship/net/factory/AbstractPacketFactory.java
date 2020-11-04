package battleship.net.factory;

import battleship.net.packet.AbstractPacket;

import java.io.DataInputStream;

public abstract class AbstractPacketFactory {

	public abstract AbstractPacket unmarshall(final DataInputStream stream);

}

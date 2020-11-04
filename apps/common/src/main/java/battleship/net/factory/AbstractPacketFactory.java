package battleship.net.factory;

import battleship.net.packet.AbstractPacket;

import java.io.DataInputStream;

public abstract class AbstractPacketFactory<T extends AbstractPacket> {

	public abstract T unmarshall(final DataInputStream stream);

}

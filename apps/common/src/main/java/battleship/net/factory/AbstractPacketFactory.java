package battleship.net.factory;

import battleship.net.packet.IPacket;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class AbstractPacketFactory<T extends IPacket> {

	public abstract T unmarshal(final DataInputStream stream) throws IOException;

}

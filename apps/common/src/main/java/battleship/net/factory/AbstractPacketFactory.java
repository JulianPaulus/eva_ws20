package battleship.net.factory;

import battleship.net.packet.AbstractPacket;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class AbstractPacketFactory<T extends AbstractPacket> {

	public abstract T unmarshal(final DataInputStream stream) throws IOException;

}

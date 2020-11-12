package battleship.net.factory;

import battleship.net.packet.IReceivePacket;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class AbstractPacketFactory<T extends IReceivePacket> {

	public abstract T unmarshal(final DataInputStream stream) throws IOException;

}

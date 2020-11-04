package battleship.net.factory;

import battleship.net.packet.AbstractPacket;

import java.io.DataInputStream;

public abstract class PacketFactory {

	public abstract AbstractPacket unmarshall(DataInputStream stream);

}

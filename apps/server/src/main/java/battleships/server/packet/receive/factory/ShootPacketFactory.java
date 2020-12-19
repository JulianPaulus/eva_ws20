package battleships.server.packet.receive.factory;

import battleships.net.factory.AbstractPacketFactory;
import battleships.server.packet.receive.ShootPacket;

import java.io.DataInputStream;
import java.io.IOException;

public class ShootPacketFactory extends AbstractPacketFactory<ShootPacket> {
	@Override
	public ShootPacket unmarshal(DataInputStream stream) throws IOException {
		return new ShootPacket(stream.readInt(), stream.readInt());
	}
}

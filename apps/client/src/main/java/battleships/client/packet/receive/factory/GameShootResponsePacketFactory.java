package battleships.client.packet.receive.factory;

import battleships.client.packet.receive.GameShootResponsePacket;
import battleships.net.factory.AbstractPacketFactory;

import java.io.DataInputStream;
import java.io.IOException;

public class GameShootResponsePacketFactory extends AbstractPacketFactory<GameShootResponsePacket> {
	@Override
	public GameShootResponsePacket unmarshal(DataInputStream stream) throws IOException {
		return new GameShootResponsePacket(stream.readBoolean(), stream.readBoolean(), stream.readBoolean(),
			stream.readBoolean(), stream.readInt(), stream.readInt());
	}
}

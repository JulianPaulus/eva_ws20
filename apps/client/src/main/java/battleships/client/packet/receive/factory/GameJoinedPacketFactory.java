package battleships.client.packet.receive.factory;

import battleships.client.packet.receive.GameJoinedPacket;
import battleships.net.factory.AbstractPacketFactory;
import battleships.util.Utils;

import java.io.DataInputStream;
import java.io.IOException;

public class GameJoinedPacketFactory extends AbstractPacketFactory<GameJoinedPacket> {
	@Override
	public GameJoinedPacket unmarshal(final DataInputStream stream) throws IOException {
		return new GameJoinedPacket(Utils.readUUIDFromStream(stream));
	}
}

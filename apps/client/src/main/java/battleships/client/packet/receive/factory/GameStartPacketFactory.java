package battleships.client.packet.receive.factory;

import battleships.client.packet.receive.GameStartPacket;
import battleships.net.factory.AbstractPacketFactory;

import java.io.DataInputStream;
import java.io.IOException;

public class GameStartPacketFactory extends AbstractPacketFactory<GameStartPacket> {
	@Override
	public GameStartPacket unmarshal(final DataInputStream stream) throws IOException {
		return new GameStartPacket(stream.readUTF());
	}
}

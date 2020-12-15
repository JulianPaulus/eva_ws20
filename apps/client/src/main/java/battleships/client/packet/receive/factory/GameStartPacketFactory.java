package battleships.client.packet.receive.factory;

import battleships.client.packet.receive.GamePlayerDoSetupPacket;
import battleships.net.factory.AbstractPacketFactory;

import java.io.DataInputStream;
import java.io.IOException;

public class GameStartPacketFactory extends AbstractPacketFactory<GamePlayerDoSetupPacket> {
	@Override
	public GamePlayerDoSetupPacket unmarshal(final DataInputStream stream) throws IOException {
		return new GamePlayerDoSetupPacket(stream.readUTF());
	}
}

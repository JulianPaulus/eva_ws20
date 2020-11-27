package battleships.server.packet.receive.factory;

import battleships.net.factory.AbstractPacketFactory;
import battleships.server.packet.receive.CreateGamePacket;

import java.io.DataInputStream;
import java.io.IOException;

public class CreateGamePacketFactory extends AbstractPacketFactory<CreateGamePacket> {

	@Override
	public CreateGamePacket unmarshal(final DataInputStream stream) throws IOException {
		return new CreateGamePacket();
	}
}

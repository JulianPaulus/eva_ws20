package battleships.server.packet.receive.factory;

import battleships.net.factory.AbstractPacketFactory;
import battleships.server.packet.receive.JoinGamePacket;
import battleships.util.Utils;

import java.io.DataInputStream;
import java.io.IOException;

public class JoinGamePacketFactory extends AbstractPacketFactory<JoinGamePacket> {
	@Override
	public JoinGamePacket unmarshal(final DataInputStream stream) throws IOException {
		return new JoinGamePacket(Utils.readUUIDFromStream(stream));
	}
}

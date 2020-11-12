package battleships.server.packet.receive.factory;

import battleships.net.factory.AbstractPacketFactory;
import battleships.server.packet.receive.LobbyListRequestPacket;

import java.io.DataInputStream;
import java.io.IOException;

public class LobbyListRequestPacketFactory extends AbstractPacketFactory<LobbyListRequestPacket> {

	@Override
	public LobbyListRequestPacket unmarshal(DataInputStream stream) throws IOException {
		return new LobbyListRequestPacket();
	}
}

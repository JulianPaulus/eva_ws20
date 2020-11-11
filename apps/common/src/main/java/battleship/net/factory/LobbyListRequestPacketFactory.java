package battleship.net.factory;

import battleship.net.packet.server.LobbyListRequestPacket;

import java.io.DataInputStream;
import java.io.IOException;

public class LobbyListRequestPacketFactory extends AbstractPacketFactory<LobbyListRequestPacket> {

	@Override
	public LobbyListRequestPacket unmarshal(DataInputStream stream) throws IOException {
		return new LobbyListRequestPacket();
	}
}

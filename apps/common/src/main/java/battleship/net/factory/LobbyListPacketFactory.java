package battleship.net.factory;

import battleship.net.packet.LobbyListPacket;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class LobbyListPacketFactory extends AbstractPacketFactory<LobbyListPacket> {
	@Override
	public LobbyListPacket unmarshal(DataInputStream stream) throws IOException {
		Set<String> lobbyList = new HashSet<>();
		short lobbyCount = stream.readShort();

		for(int i = 0; i < lobbyCount; i++) {
			lobbyList.add(stream.readUTF());
		}

		return new LobbyListPacket(lobbyList);
	}
}

package battleship.client.packet.receive.factory;

import battleship.client.packet.receive.LobbyListPacket;
import battleship.net.factory.AbstractPacketFactory;
import battleship.packet.PacketLobby;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class LobbyListPacketFactory extends AbstractPacketFactory<LobbyListPacket> {

	@Override
	public LobbyListPacket unmarshal(DataInputStream stream) throws IOException {
		Set<PacketLobby> lobbyList = new HashSet<>();
		short lobbyCount = stream.readShort();

		for(int i = 0; i < lobbyCount; i++) {
			lobbyList.add(new PacketLobby(stream.readInt(), stream.readUTF()));
		}

		return new LobbyListPacket(lobbyList);
	}
}

package battleships.client.packet.receive.factory;

import battleships.client.packet.receive.LobbyListPacket;
import battleships.net.factory.AbstractPacketFactory;
import battleships.packet.PacketLobby;
import battleships.util.Constants;
import battleships.util.Utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LobbyListPacketFactory extends AbstractPacketFactory<LobbyListPacket> {

	@Override
	public LobbyListPacket unmarshal(final DataInputStream stream) throws IOException {
		Set<PacketLobby> lobbyList = new HashSet<>();
		short lobbyCount = stream.readShort();

		for(int i = 0; i < lobbyCount; i++) {
			lobbyList.add(new PacketLobby(readUUID(stream), stream.readUTF()));
		}

		return new LobbyListPacket(lobbyList);
	}

	private UUID readUUID(final DataInputStream dos) throws IOException {
		byte[] idBytes = new byte[Constants.UUID_BYTE_COUNT];
		for (int i = 0; i < idBytes.length; i++) {
			idBytes[i] = dos.readByte();
		}

		return Utils.getUUIDFromBytes(idBytes);
	}
}

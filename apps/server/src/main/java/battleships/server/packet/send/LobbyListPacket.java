package battleships.server.packet.send;

import battleships.Constants;
import battleships.net.packet.SendPacket;
import battleships.packet.PacketLobby;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;

public class LobbyListPacket extends SendPacket {

	public static final byte IDENTIFIER = Constants.Identifiers.LOBBY_LIST_RESPONSE;
	private final Set<PacketLobby> lobbySet;

	public LobbyListPacket(final Set<PacketLobby> lobbySet) {
		this.lobbySet = lobbySet;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected DataOutputStream writeContent(DataOutputStream dos) throws IOException {
		dos.writeShort(lobbySet.size());
		for (final PacketLobby lobby : lobbySet) {
			dos.writeInt(lobby.getId());
			dos.writeUTF(lobby.getName());
		}
		return dos;
	}
}

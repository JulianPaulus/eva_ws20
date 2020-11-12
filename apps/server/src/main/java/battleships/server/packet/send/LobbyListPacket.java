package battleships.server.packet.send;

import battleship.net.connection.AuthenticatedConnection;
import battleship.net.packet.AbstractLobbyPacket;
import battleship.packet.PacketLobby;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;

public class LobbyListPacket extends AbstractLobbyPacket {

	public static final byte IDENTIFIER = 0x1;
	private final Set<PacketLobby> lobbySet;

	public LobbyListPacket(Set<PacketLobby> lobbySet) {
		this.lobbySet = lobbySet;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected DataOutputStream writeContent(DataOutputStream dos) throws IOException {
		dos.writeShort(lobbySet.size());
		for (PacketLobby lobby : lobbySet) {
			dos.writeInt(lobby.getId());
			dos.writeUTF(lobby.getName());
		}
		return dos;
	}

	@Override
	public void act(AuthenticatedConnection connection) {
		//Nothing
	}
}

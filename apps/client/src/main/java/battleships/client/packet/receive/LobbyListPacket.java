package battleships.client.packet.receive;

import battleships.client.lobby.LobbyListController;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.packet.PacketLobby;
import battleships.util.Constants;

import java.util.Set;

public class LobbyListPacket implements IPreAuthReceivePacket {

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
	public void act(final Connection connection) {
		LobbyListController llc = LobbyListController.getNewestInstance();
		if(llc != null) {
			llc.setLobbies(this.lobbySet);
		}
	}
}

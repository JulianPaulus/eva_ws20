package battleships.client.packet.receive;

import battleships.client.lobby.LobbyListController;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.packet.PacketLobby;

import java.util.Set;

public class LobbyListPacket implements IPreAuthReceivePacket {

	public static final byte IDENTIFIER = 0x1;
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

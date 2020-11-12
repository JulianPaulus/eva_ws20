package battleship.client.packet.receive;

import battleship.client.lobby.LobbyListController;
import battleship.net.connection.Connection;
import battleship.net.packet.IPreAuthReceivePacket;
import battleship.packet.PacketLobby;

import java.util.Set;

public class LobbyListPacket implements IPreAuthReceivePacket {

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
	public void act(Connection connection) {
		LobbyListController llc = LobbyListController.getNewestInstance();
		if(llc != null) {
			llc.setLobbies(this.lobbySet);
		}
	}
}

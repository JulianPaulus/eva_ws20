package battleships.server.packet.receive;

import battleships.net.connection.AuthenticatedConnection;
import battleships.net.packet.ILobbyReceivePacket;
import battleships.packet.Game;
import battleships.server.packet.send.LobbyListPacket;
import battleships.server.service.LobbyService;
import battleships.util.Constants;

import java.util.List;

public class LobbyListRequestPacket implements ILobbyReceivePacket {
	public static final byte IDENTIFIER = Constants.Identifiers.LOBBY_LIST_REQUEST;

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(final AuthenticatedConnection connection) {
		List<Game> games = LobbyService.getInstance().getGames();
		connection.writePacket(new LobbyListPacket(games));
	}
}

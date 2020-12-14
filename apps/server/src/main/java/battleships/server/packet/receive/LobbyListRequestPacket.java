package battleships.server.packet.receive;

import battleships.server.connection.AuthenticatedConnection;
import battleships.server.game.Game;
import battleships.server.packet.ILobbyReceivePacket;
import battleships.server.packet.send.LobbyListPacket;
import battleships.server.service.GameService;
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
		List<Game> games = GameService.getInstance().getOpenGames();
		connection.writePacket(new LobbyListPacket(games));
	}
}

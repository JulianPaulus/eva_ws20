package battleships.server.packet.receive;

import battleships.net.connection.AuthenticatedConnection;
import battleships.net.connection.GameConnection;
import battleships.net.packet.ILobbyReceivePacket;
import battleships.packet.Game;
import battleships.server.service.LobbyService;
import battleships.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class CreateGamePacket implements ILobbyReceivePacket {
	public static final byte IDENTIFIER = Constants.Identifiers.CREATE_GAME_REQUEST;
	private static final Logger logger = LoggerFactory.getLogger(CreateGamePacket.class);
	private static final LobbyService LOBBY_SERVICE = LobbyService.getInstance();

	@Override
	public void act(final AuthenticatedConnection connection) {
		Optional<Game> gameCheck = LOBBY_SERVICE.getGameByUsername(connection.getPlayer().getUsername());
		if (gameCheck.isPresent()) {
			logger.warn("user {} tried to create a game while already in a game", connection.getPlayer().getUsername());
			return;
		}

		Game game = new Game(connection.getPlayer());
		GameConnection gameConnection = new GameConnection(connection, game);
		logger.info("creating game '{}'", connection.getPlayer().getUsername());
		LOBBY_SERVICE.registerGame(game);
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}
}

package battleships.server.packet.receive;

import battleships.server.connection.AuthenticatedConnection;
import battleships.server.connection.GameConnection;
import battleships.server.game.Game;
import battleships.server.packet.ILobbyReceivePacket;
import battleships.server.packet.send.GameJoinedPacket;
import battleships.server.service.GameService;
import battleships.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class CreateGamePacket implements ILobbyReceivePacket {
	public static final byte IDENTIFIER = Constants.Identifiers.CREATE_GAME_REQUEST;
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateGamePacket.class);
	private static final GameService LOBBY_SERVICE = GameService.getInstance();

	@Override
	public void act(final AuthenticatedConnection connection) {
		Optional<Game> gameCheck = LOBBY_SERVICE.getGameByUsername(connection.getPlayer().getUsername());
		if (gameCheck.isPresent()) {
			LOGGER.warn("user {} tried to create a game while already in a game", connection.getPlayer().getUsername());
			return;
		}

		Game game = new Game(connection.getPlayer());
		GameConnection gameConnection = new GameConnection(connection, game);

		LOGGER.info("created game '{}'", connection.getPlayer().getUsername());
		LOBBY_SERVICE.registerGame(game);

		connection.writePacket(new GameJoinedPacket(game.getId()));
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}
}

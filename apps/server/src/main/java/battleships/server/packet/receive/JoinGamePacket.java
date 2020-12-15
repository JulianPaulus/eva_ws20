package battleships.server.packet.receive;

import battleships.server.connection.AuthenticatedConnection;
import battleships.server.connection.GameConnection;
import battleships.server.exception.ServerException;
import battleships.server.game.Game;
import battleships.server.packet.ILobbyReceivePacket;
import battleships.server.packet.send.GameJoinedPacket;
import battleships.server.packet.send.ServerErrorPacket;
import battleships.server.service.GameService;
import battleships.util.Constants;
import battleships.util.ServerErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

public class JoinGamePacket implements ILobbyReceivePacket {

	public static final byte IDENTIFIER = Constants.Identifiers.GAME_JOIN_REQUEST;

	private static final Logger LOGGER = LoggerFactory.getLogger(JoinGamePacket.class);
	private static final GameService LOBBY_SERVICE = GameService.getInstance();

	private final UUID gameId;

	public JoinGamePacket(final UUID gameId) {
		this.gameId = gameId;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(final AuthenticatedConnection connection) {
		Optional<Game> gameCheck = LOBBY_SERVICE.getGameByUsername(connection.getPlayer().getUsername());
		if (gameCheck.isPresent()) {
			LOGGER.warn("user {} tried to create a game while already in a game", connection.getPlayer().getUsername());
			return;
		}

		try {
			Game game = LOBBY_SERVICE.getGameById(gameId).orElseThrow(() -> new ServerException("Game not found!"));
			GameConnection gameConnection = new GameConnection(connection, game);
			game.addGuest(gameConnection.getPlayer());

			LOGGER.debug("{} joined game({}) hosted by {}", connection.getPlayer().getUsername(), game.getId(), game.getHost().getUsername());
		} catch (final ServerException e) {
			connection.writePacket(new ServerErrorPacket(ServerErrorType.CRITICAL, e.getMessage()));
		}

	}
}

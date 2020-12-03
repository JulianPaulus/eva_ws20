package battleships.server.connection;

import battleships.server.connection.packethandler.GamePacketHandler;
import battleships.server.game.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameConnection extends AuthenticatedConnection {

	private static final Logger logger = LoggerFactory.getLogger(GameConnection.class);

	private final Game game;

	public GameConnection(final AuthenticatedConnection connection, final Game game) {
		super(connection);
		super.packetHandler = new GamePacketHandler();
		super.reader.setConnection(this);
		this.game = game;

		setObservers(connection.getObservers());

		logger.debug("creating GameConnection for {} in game {}", getPlayer().getUsername(), game.getId().toString());
	}

	public Game getGame() {
		return game;
	}

	@Override
	public String toString() {
		return "GameConnection{" +
			"game=" + game +
			", socket=" + socket +
			", reader=" + reader +
			", writer=" + writer +
			", packetHandler=" + packetHandler +
			", closed=" + closed +
			'}';
	}
}

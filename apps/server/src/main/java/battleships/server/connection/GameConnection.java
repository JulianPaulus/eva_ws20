package battleships.server.connection;

import battleships.server.connection.packethandler.GamePacketHandler;
import battleships.server.game.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

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

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		GameConnection that = (GameConnection) o;
		return game.equals(that.game);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), game);
	}
}

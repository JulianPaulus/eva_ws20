package battleships.server.connection;

import battleships.net.connection.Connection;
import battleships.server.connection.packethandler.LobbyPacketHandler;
import battleships.server.game.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticatedConnection extends Connection {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticatedConnection.class);

	private final Player player;

	public AuthenticatedConnection(final Connection connection, final Player player) {
		super(connection);
		super.packetHandler = new LobbyPacketHandler();
		super.reader.setConnection(this);
		this.player = player;

		setObservers(connection.getObservers());

		logger.debug("creating AuthenticatedConnection for {}", player.getUsername());
	}

	protected AuthenticatedConnection(final AuthenticatedConnection connection) {
		this(connection, connection.player);
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public String toString() {
		return "AuthenticatedConnection{" +
			"player=" + player +
			", socket=" + socket +
			", reader=" + reader +
			", writer=" + writer +
			", packetHandler=" + packetHandler +
			", closed=" + closed +
			'}';
	}
}

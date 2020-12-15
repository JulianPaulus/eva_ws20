package battleships.server.connection;

import battleships.net.connection.Connection;
import battleships.server.connection.packethandler.LobbyPacketHandler;
import battleships.server.game.Player;
import battleships.server.service.ConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class AuthenticatedConnection extends Connection {

	protected static final ConnectionService CONNECTION_SERVICE = ConnectionService.getInstance();
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticatedConnection.class);

	private final Player player;

	public AuthenticatedConnection(final Connection connection, final Player player) {
		super(connection);
		super.packetHandler = new LobbyPacketHandler();
		super.reader.setConnection(this);
		this.player = player;

		CONNECTION_SERVICE.registerConnection(this);

		LOGGER.debug("created AuthenticatedConnection for {}", player.getUsername());
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

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		AuthenticatedConnection that = (AuthenticatedConnection) o;
		return player.equals(that.player);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), player);
	}
}

package battleships.net.connection;

import battleships.net.connection.packethandler.LobbyPacketHandler;
import battleships.packet.Player;

public class AuthenticatedConnection extends Connection {
	private final Player player;

	public AuthenticatedConnection(final Connection connection, final Player player) {
		super(connection);
		super.packetHandler = new LobbyPacketHandler();
		super.reader.setConnection(this);
		this.player = player;
	}

	protected AuthenticatedConnection(final AuthenticatedConnection connection) {
		this(connection, connection.player);
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

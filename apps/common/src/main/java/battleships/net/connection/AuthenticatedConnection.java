package battleships.net.connection;

import battleships.net.connection.packethandler.LobbyPacketHandler;
import battleships.packet.Player;

public class AuthenticatedConnection extends Connection {
	private Player player;

	public AuthenticatedConnection(Connection connection, Player player) {
		super(connection);
		super.packetHandler = new LobbyPacketHandler();
		super.reader.setConnection(this);
		this.player = player;
	}

	protected AuthenticatedConnection(AuthenticatedConnection connection) {
		this(connection, connection.player);
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}
}

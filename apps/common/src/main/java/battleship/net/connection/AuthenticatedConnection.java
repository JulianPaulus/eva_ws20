package battleship.net.connection;

import battleship.net.connection.packethandler.LobbyPacketHandler;
import battleship.packet.Player;

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

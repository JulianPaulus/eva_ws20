package battleship.net.connection;

import battleship.net.connection.packethandler.LobbyPacketHandler;
import battleship.packet.Player;

public class AuthenticatedConnection extends Connection {
	private Player player;

	public AuthenticatedConnection(Connection connection, Player player) {
		super(connection);
		this.player = player;
	}

	protected AuthenticatedConnection(AuthenticatedConnection connection) {
		super(connection);
		super.packetHandler = new LobbyPacketHandler();
		this.player = connection.player;

		super.reader.setConnection(this);
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}
}

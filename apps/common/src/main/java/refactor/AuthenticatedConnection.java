package refactor;

import battleship.packet.Player;

public class AuthenticatedConnection extends Connection {
	private Player player;

	public AuthenticatedConnection(Connection connection, Player player) {
		super(connection);
		this.player = player;
	}

	protected AuthenticatedConnection(AuthenticatedConnection connection) {
		super(connection);
		this.player = connection.player;
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}
}

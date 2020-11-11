package refactor;

import battleship.packet.Game;

public class GameConnection extends AuthenticatedConnection {
	private Game game;

	public GameConnection(AuthenticatedConnection connection, Game game) {
		super(connection);
		this.game = game;
	}

	public Game getGame() {
		return game;
	}
}

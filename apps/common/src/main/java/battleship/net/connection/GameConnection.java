package battleship.net.connection;

import battleship.net.connection.packethandler.GamePacketHandler;
import battleship.packet.Game;

public class GameConnection extends AuthenticatedConnection {
	private Game game;

	public GameConnection(AuthenticatedConnection connection, Game game) {
		super(connection);
		super.packetHandler = new GamePacketHandler();
		super.reader.setConnection(this);
		this.game = game;
	}

	public Game getGame() {
		return game;
	}
}

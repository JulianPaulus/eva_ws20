package battleships.net.connection;

import battleships.net.connection.packethandler.GamePacketHandler;
import battleships.packet.Game;

public class GameConnection extends AuthenticatedConnection {
	private final Game game;

	public GameConnection(final AuthenticatedConnection connection, final Game game) {
		super(connection);
		super.packetHandler = new GamePacketHandler();
		super.reader.setConnection(this);
		this.game = game;
	}

	public Game getGame() {
		return game;
	}
}

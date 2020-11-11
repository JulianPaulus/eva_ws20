package battleships.server.socket;

import battleship.packet.Game;
import battleship.util.AuthenticatedConnection;
import battleship.util.GameConnection;

public class GameThread extends Thread {
	private GameConnection[] playerConnections = new GameConnection[2];
	private Game game = new Game();

	public GameThread(AuthenticatedConnection playerConnection) {
		playerConnections[0] = new GameConnection(playerConnection, this.game);
	}



}

package battleships.client.packet.receive;

import battleships.client.game.GameWindow;
import battleships.model.Ship;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;

public class EnemyShipPositionsPacket implements IPreAuthReceivePacket {

	public static final byte IDENTIFIER = Constants.Identifiers.GAME_OVER_SHIP_LOCATIONS;

	private final Ship[] enemyShips;

	public EnemyShipPositionsPacket(final Ship[] enemyShips) {
		this.enemyShips = enemyShips;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(final Connection connection) {
		GameWindow.getInstance().setEnemyShips(enemyShips);
	}
}

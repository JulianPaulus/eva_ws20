package battleships.server.packet.send;

import battleships.model.Ship;
import battleships.net.packet.SendPacket;
import battleships.util.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class EnemyShipPositionsPacket extends SendPacket {

	private final Ship[] enemyShips;

	public EnemyShipPositionsPacket(final Ship[] enemyShips) {
		this.enemyShips = enemyShips;
	}

	@Override
	public byte getIdentifier() {
		return Constants.Identifiers.GAME_OVER_SHIP_LOCATIONS;
	}

	@Override
	protected void writeContent(DataOutputStream dos) throws IOException {
		for (final Ship ship : enemyShips) {
			ship.writeToStream(dos);
		}
	}
}

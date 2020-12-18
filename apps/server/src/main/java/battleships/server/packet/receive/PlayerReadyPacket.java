package battleships.server.packet.receive;

import battleships.model.Ship;
import battleships.net.exception.IllegalShipPositionException;
import battleships.server.connection.GameConnection;
import battleships.server.packet.IGameReceivePacket;
import battleships.server.packet.send.IllegalShipPositionPacket;
import battleships.util.Constants;

public class PlayerReadyPacket implements IGameReceivePacket {
	public static final byte IDENTIFIER = Constants.Identifiers.PLAYER_READY_REQUEST;
	private final Ship[] ships;

	public PlayerReadyPacket(Ship[] ships) {
		this.ships = ships;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(GameConnection connection) {
		try {
			connection.getGame().setShips(connection.getPlayer().getId(), this.ships);
		} catch (IllegalShipPositionException e) {
			connection.writePacket(new IllegalShipPositionPacket());
		}
	}
}

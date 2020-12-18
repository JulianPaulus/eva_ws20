package battleships.client.packet.send;

import battleships.model.Ship;
import battleships.net.packet.SendPacket;
import battleships.util.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerReadyPacket extends SendPacket {
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
	protected DataOutputStream writeContent(DataOutputStream dos) throws IOException {
		for(Ship ship : ships) {
			dos.writeInt(ship.getType().getSize());
			dos.writeInt(ship.getXCoordinate());
			dos.writeInt(ship.getYCoordinate());
			dos.writeBoolean(ship.isHorizontal());
		}
		return dos;
	}
}

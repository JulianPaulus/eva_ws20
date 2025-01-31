package battleships.client.packet.send;

import battleships.model.Ship;
import battleships.net.packet.SendPacket;
import battleships.util.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerReadyPacket extends SendPacket {
	public static final byte IDENTIFIER = Constants.Identifiers.PLAYER_READY_REQUEST;
	private final Ship[] ships;

	public PlayerReadyPacket(final Ship[] ships) {
		this.ships = ships;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected void writeContent(DataOutputStream dos) throws IOException {
		for(final Ship ship : ships) {
			ship.writeToStream(dos);
		}
	}
}

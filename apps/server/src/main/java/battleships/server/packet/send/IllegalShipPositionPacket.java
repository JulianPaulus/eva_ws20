package battleships.server.packet.send;

import battleships.net.packet.SendPacket;
import battleships.util.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class IllegalShipPositionPacket extends SendPacket {
	public static final byte IDENTIFIER = Constants.Identifiers.GAME_PLAYER_ILLEGAL_SHIP_POSITION;

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected void writeContent(DataOutputStream dos) throws IOException {
	}
}

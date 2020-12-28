package battleships.client.packet.send;

import battleships.net.packet.SendPacket;
import battleships.util.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class CreateGamePacket extends SendPacket {
	public static final byte IDENTIFIER = Constants.Identifiers.CREATE_GAME_REQUEST;

	@Override
	protected void writeContent(final DataOutputStream dos) {}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}
}

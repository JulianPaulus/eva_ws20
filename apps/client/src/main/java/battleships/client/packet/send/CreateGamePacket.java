package battleships.client.packet.send;

import battleships.net.packet.SendPacket;
import battleships.util.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class CreateGamePacket extends SendPacket {
	public static final byte IDENTIFIER = Constants.Identifiers.CREATE_GAME_REQUEST;

	@Override
	protected DataOutputStream writeContent(final DataOutputStream dos) throws IOException {
		return dos;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}
}

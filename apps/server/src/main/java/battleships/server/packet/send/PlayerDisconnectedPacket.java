package battleships.server.packet.send;

import battleships.net.packet.SendPacket;
import battleships.util.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerDisconnectedPacket extends SendPacket {

	public static final byte IDENTIFIER = Constants.Identifiers.PLAYER_DISCONNECTED;

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected DataOutputStream writeContent(final DataOutputStream dos) throws IOException {
		return dos;
	}
}

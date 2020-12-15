package battleships.server.packet.send;

import battleships.net.packet.SendPacket;
import battleships.util.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class GameOtherPlayerSetupPacket extends SendPacket {
	public static final byte IDENTIFIER = Constants.Identifiers.GAME_PLAYER_OTHER_PLAYER_SETUP_MESSAGE;

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected DataOutputStream writeContent(DataOutputStream dos) throws IOException {
		return dos;
	}
}

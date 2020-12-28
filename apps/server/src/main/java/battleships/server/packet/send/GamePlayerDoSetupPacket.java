package battleships.server.packet.send;

import battleships.net.packet.SendPacket;
import battleships.util.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class GamePlayerDoSetupPacket extends SendPacket {

	public static final byte IDENTIFIER = Constants.Identifiers.GAME_PLAYER_DO_SETUP_MESSAGE;

	private final String otherPlayerName;

	public GamePlayerDoSetupPacket(final String otherPlayerName) {
		this.otherPlayerName = otherPlayerName;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected void writeContent(final DataOutputStream dos) throws IOException {
		dos.writeUTF(otherPlayerName);
	}
}

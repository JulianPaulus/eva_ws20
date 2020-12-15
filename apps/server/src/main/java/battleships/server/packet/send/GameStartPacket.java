package battleships.server.packet.send;

import battleships.net.packet.SendPacket;
import battleships.util.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class GameStartPacket extends SendPacket {

	public static final byte IDENTIFIER = Constants.Identifiers.START_GAME_MESSAGE;

	private final String otherPlayerName;

	public GameStartPacket(final String otherPlayerName) {
		this.otherPlayerName = otherPlayerName;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected DataOutputStream writeContent(final DataOutputStream dos) throws IOException {
		dos.writeUTF(otherPlayerName);
		return dos;
	}
}

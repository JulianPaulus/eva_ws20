package battleships.server.packet.send;

import battleships.net.packet.SendPacket;
import battleships.util.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class GameEnemiesTurnPacket extends SendPacket {
	public static final byte IDENTIFIER = Constants.Identifiers.GAME_ENEMIES_TURN;


	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected DataOutputStream writeContent(DataOutputStream dos) throws IOException {
		return dos;
	}
}

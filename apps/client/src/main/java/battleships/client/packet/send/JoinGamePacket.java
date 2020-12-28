package battleships.client.packet.send;

import battleships.net.packet.SendPacket;
import battleships.util.Constants;
import battleships.util.Utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class JoinGamePacket extends SendPacket {

	public static final byte IDENTIFIER = Constants.Identifiers.GAME_JOIN_REQUEST;

	private final UUID gameId;

	public JoinGamePacket(final UUID gameId) {
		this.gameId = gameId;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected void writeContent(final DataOutputStream dos) throws IOException {
		Utils.writeUUIDToStream(dos, gameId);
	}
}

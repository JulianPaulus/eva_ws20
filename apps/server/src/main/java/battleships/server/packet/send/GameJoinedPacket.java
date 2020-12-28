package battleships.server.packet.send;

import battleships.net.packet.SendPacket;
import battleships.util.Constants;
import battleships.util.Utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class GameJoinedPacket extends SendPacket {

	public static final byte IDENTIFIER = Constants.Identifiers.GAME_JOIN_RESPONSE;

	private final UUID gameId;

	public GameJoinedPacket(final UUID gameId) {
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

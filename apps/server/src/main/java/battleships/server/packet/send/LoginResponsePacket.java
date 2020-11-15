package battleships.server.packet.send;

import battleships.Constants;
import battleships.net.packet.SendPacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class LoginResponsePacket extends SendPacket {

	public static final byte IDENTIFIER = Constants.Identifiers.LOGIN_RESPONSE;

	private final int playerId;
	private final boolean successful;

	public LoginResponsePacket(final int playerId, final boolean successful) {
		this.playerId = playerId;
		this.successful = successful;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected DataOutputStream writeContent(final DataOutputStream dos) throws IOException {
		dos.writeInt(playerId);
		dos.writeBoolean(successful);

		return dos;
	}

	public int getPlayerId() {
		return playerId;
	}

	public boolean isSuccessful() {
		return successful;
	}
}

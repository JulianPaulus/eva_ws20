package battleships.server.packet.send;

import battleship.net.packet.SendPacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class LoginResponsePacket extends SendPacket {

	public static final byte IDENTIFIER = 0x4;

	private final int playerId;
	private final boolean successful;

	public LoginResponsePacket(int playerId, boolean successful) {
		this.playerId = playerId;
		this.successful = successful;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected DataOutputStream writeContent(DataOutputStream dos) throws IOException {
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

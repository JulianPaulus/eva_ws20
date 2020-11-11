package battleship.net.packet;

import battleship.net.ConnectionSide;
import battleship.net.connection.Connection;

import java.io.DataOutputStream;
import java.io.IOException;

public class LoginResponsePacket extends AbstractPacket<Connection> {

	private final int playerId;
	private final boolean successful;

	public LoginResponsePacket(int playerId, boolean successful) {
		this.playerId = playerId;
		this.successful = successful;
	}

	@Override
	public void act(Connection connection) {

	}

	@Override
	public ConnectionSide getConnectionSide() {
		return null;
	}

	@Override
	public byte getIdentifier() {
		return 0;
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

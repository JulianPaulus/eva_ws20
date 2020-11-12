package battleship.client.packet.receive;

import battleship.net.connection.Connection;
import battleship.net.packet.AbstractPacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class LoginResponsePacket extends AbstractPacket<Connection> {

	public static final byte IDENTIFIER = 0x4;

	private final int playerId;
	private final boolean successful;

	public LoginResponsePacket(int playerId, boolean successful) {
		this.playerId = playerId;
		this.successful = successful;
	}

	@Override
	public void act(Connection connection) {
		System.out.println("response");
		System.out.println(playerId);
		System.out.println(successful);
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected DataOutputStream writeContent(DataOutputStream dos) throws IOException {
		//Nothing
		return dos;
	}

	public int getPlayerId() {
		return playerId;
	}

	public boolean isSuccessful() {
		return successful;
	}
}

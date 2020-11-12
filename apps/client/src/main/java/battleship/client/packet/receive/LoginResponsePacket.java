package battleship.client.packet.receive;

import battleship.net.connection.Connection;
import battleship.net.packet.IPacket;
import battleship.net.packet.IPreAuthReceivePacket;
import battleship.net.packet.IReceivePacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class LoginResponsePacket implements IPreAuthReceivePacket {

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

	public int getPlayerId() {
		return playerId;
	}

	public boolean isSuccessful() {
		return successful;
	}
}

package battleship.client.packet.send;

import battleship.net.connection.Connection;
import battleship.net.packet.IPacket;
import battleship.net.packet.SendPacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class LoginPacket extends SendPacket {
	public static final byte IDENTIFIER = 0x3;

	private final String username;
	private final String password;

	public LoginPacket(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected DataOutputStream writeContent(DataOutputStream dos) throws IOException {
		dos.writeUTF(username);
		dos.writeUTF(password);
		return dos;
	}
}

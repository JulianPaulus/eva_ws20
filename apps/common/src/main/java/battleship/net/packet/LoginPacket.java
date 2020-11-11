package battleship.net.packet;

import battleship.net.ConnectionSide;
import refactor.Connection;

import java.io.DataOutputStream;
import java.io.IOException;

public class LoginPacket extends AbstractPacket<Connection> {
	public static final byte IDENTIFIER = 0x3;

	private final String username;
	private final String password;

	public LoginPacket(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public void act(Connection connection) {

	}

	@Override
	public ConnectionSide getConnectionSide() {
		return ConnectionSide.SERVER;
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

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}

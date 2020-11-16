package battleships.client.packet.send;

import battleships.net.packet.SendPacket;
import battleships.util.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class LoginPacket extends SendPacket {
	public static final byte IDENTIFIER = Constants.Identifiers.LOGIN_REQUEST;

	private final String username;
	private final String password;

	public LoginPacket(final String username, final String password) {
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

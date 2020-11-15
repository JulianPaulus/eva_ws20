package battleships.server.packet.receive;

import battleships.Constants;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;

public class RegisterPacket implements IPreAuthReceivePacket {

	public static final byte IDENTIFIER = Constants.Identifiers.REGISTER_REQUEST;

	private final String username;
	private final String password;

	public RegisterPacket(final String username, final String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public void act(final Connection connection) {
		System.out.println("register");
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}
}

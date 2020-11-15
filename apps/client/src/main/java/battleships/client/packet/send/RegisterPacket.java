package battleships.client.packet.send;

import battleships.Constants;

public class RegisterPacket extends LoginPacket {

	public static final byte IDENTIFIER = Constants.Identifiers.REGISTER_REQUEST;

	public RegisterPacket(final String username, final  String password) {
		super(username, password);
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}
}

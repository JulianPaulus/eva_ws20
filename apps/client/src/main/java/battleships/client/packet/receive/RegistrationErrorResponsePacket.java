package battleships.client.packet.receive;

import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;
import battleships.util.RegistrationError;

public class RegistrationErrorResponsePacket implements IPreAuthReceivePacket {

	public static final byte IDENTIFIER = Constants.Identifiers.REGISTER_ERROR_RESPONSE;

	private final boolean successful;
	private final RegistrationError registrationError;

	public RegistrationErrorResponsePacket(final boolean successful, final RegistrationError registrationError) {
		this.successful = successful;
		this.registrationError = registrationError;
	}

	@Override
	public void act(final Connection connection) {

	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}
}

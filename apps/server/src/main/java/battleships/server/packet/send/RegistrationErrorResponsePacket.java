package battleships.server.packet.send;

import battleships.net.packet.SendPacket;
import battleships.util.Constants;
import battleships.util.RegistrationError;

import java.io.DataOutputStream;
import java.io.IOException;

public class RegistrationErrorResponsePacket extends SendPacket {

	public static final byte IDENTIFIER = Constants.Identifiers.REGISTER_ERROR_RESPONSE;

	private final boolean successful;
	private final RegistrationError registrationError;
	private final String message;

	public RegistrationErrorResponsePacket(final boolean successful, final RegistrationError registrationError, final String message) {
		this.successful = successful;
		this.registrationError = registrationError;
		this.message = message;
	}

	@Override
	protected DataOutputStream writeContent(final DataOutputStream dos) throws IOException {
		dos.writeBoolean(successful);
		dos.writeUTF(registrationError.toString());
		dos.writeUTF(message);

		return dos;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}
}

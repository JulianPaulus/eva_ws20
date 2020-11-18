package battleships.client.packet.receive;

import battleships.client.ClientMain;
import battleships.client.util.ErrorDialog;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;
import battleships.util.RegistrationError;

public class RegistrationErrorResponsePacket implements IPreAuthReceivePacket {

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
	public void act(final Connection connection) {
		ErrorDialog.show(ClientMain.getInstance().getStage(), message);
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}
}

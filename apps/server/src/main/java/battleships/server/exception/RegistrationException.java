package battleships.server.exception;

import battleships.util.RegistrationError;

public class RegistrationException extends Exception {

	private final RegistrationError registrationError;

	public RegistrationException(final String message, final RegistrationError registrationError) {
		super(message);
		this.registrationError = registrationError;
	}

	public RegistrationError getRegistrationError() {
		return registrationError;
	}
}

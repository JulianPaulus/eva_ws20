package battleships.net.exception;

public class IllegalPacketTypeException extends IllegalArgumentException {

	public IllegalPacketTypeException(final String message) {
		super(message);
	}
}

package battleships.server.exception;

public class IllegalShipPositionException extends IllegalArgumentException {

	public IllegalShipPositionException(String message) {
		super(message);
	}

}

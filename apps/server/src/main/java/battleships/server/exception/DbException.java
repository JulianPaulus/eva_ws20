package battleships.server.exception;

public class DbException extends RuntimeException {

	public DbException(String message) {
		super(message);
	}

	public DbException(String message, Throwable e) {
		super(message, e);
	}
}

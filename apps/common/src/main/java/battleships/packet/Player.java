package battleships.packet;

public class Player {
	private final int id;
	private final String username;
	private final String password;

	public Player(final int id, final String username, final String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}

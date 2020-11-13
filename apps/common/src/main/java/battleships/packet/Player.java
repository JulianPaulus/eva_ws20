package battleships.packet;

public class Player {
	private final int id;
	private final String username;

	public Player(final int id, final String username) {
		this.id = id;
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}
}

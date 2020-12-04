package battleships.server.game;

import java.util.Objects;

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

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Player player = (Player) o;
		return id == player.id && username.equals(player.username) && password.equals(player.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, username, password);
	}
}

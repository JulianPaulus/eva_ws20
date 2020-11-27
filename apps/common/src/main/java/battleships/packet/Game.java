package battleships.packet;

import java.util.Optional;
import java.util.UUID;

public class Game {

	private final UUID id;
	private final Player host;

	private Player guest;

	public Game(final Player host) {
		this.id = UUID.randomUUID();
		this.host = host;
	}

	public UUID getId() {
		return id;
	}

	public Player getHost() {
		return host;
	}

	public Optional<Player> getGuest() {
		return Optional.ofNullable(guest);
	}
}

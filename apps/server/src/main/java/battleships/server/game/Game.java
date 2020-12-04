package battleships.server.game;

import battleships.server.exception.ServerException;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

// TODO threadsafety
public class Game {

	private final UUID id;
	private final Player host;

	private Player guest;

	public Game(final Player host) {
		this.id = UUID.randomUUID();
		this.host = host;
	}

	public void addGuest(final Player guest) throws ServerException {
		if (this.guest == null) {
			this.guest = guest;

			// TODO send join message to host
		} else {
			throw new ServerException("Game is full!");
		}
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

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Game game = (Game) o;
		return id.equals(game.id) && host.equals(game.host) && Objects.equals(guest, game.guest);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, host, guest);
	}
}

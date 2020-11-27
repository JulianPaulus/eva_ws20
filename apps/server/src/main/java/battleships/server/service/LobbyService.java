package battleships.server.service;

import battleships.packet.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// TODO: make this threadsafe
// and maybe rename it to GameService, because all lobbies are games ._.
public class LobbyService {

	private static LobbyService instance;

	private final List<Game> games;

	public LobbyService() {
		this.games = new ArrayList<>();
	}

	public synchronized static LobbyService getInstance() {
		if (instance == null) {
			instance = new LobbyService();
		}
		return instance;
	}

	public void registerGame(final Game game) {
		games.add(game);
	}

	public List<Game> getGames() {
		return games;
	}

	public Optional<Game> getGameByUsername(final String name) {
		return games.stream().filter(game -> game.getHost().getUsername().equals(name) || (game.getGuest()
			.isPresent() && game.getGuest().get().getUsername().equals(name))).findFirst();
	}
}

package battleships.server.service;

import battleships.server.game.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

// TODO: make this threadsafe
public class GameService {

	private static GameService instance;

	private final List<Game> games;

	public GameService() {
		this.games = new ArrayList<>();
	}

	public synchronized static GameService getInstance() {
		if (instance == null) {
			instance = new GameService();
		}
		return instance;
	}

	public synchronized void registerGame(final Game game) {
		games.add(game);
	}

	public synchronized List<Game> getGames() {
		return games;
	}

	public synchronized List<Game> getGamesWithOnePlayer() {
		return games.stream().filter(game -> !game.getGuest().isPresent()).collect(Collectors.toList());
	}

	public synchronized Optional<Game> getGameById(final UUID id) {
		return games.stream().filter(game -> game.getId().equals(id)).findFirst();
	}

	public synchronized Optional<Game> getGameByUsername(final String name) {
		return games.stream().filter(game -> game.getHost().getUsername().equals(name) || (game.getGuest()
			.isPresent() && game.getGuest().get().getUsername().equals(name))).findFirst();
	}
}

package battleships.server.service;

import battleships.server.game.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

// TODO: make this threadsafe
public class GameService {

	private final static GameService INSTANCE = new GameService();

	private final List<Game> games;
	private final ReadWriteLock gamesLock;

	private GameService() {
		this.games = new ArrayList<>();
		this.gamesLock = new ReentrantReadWriteLock();
	}

	public static GameService getInstance() {
		return INSTANCE;
	}

	public void registerGame(final Game game) {
		try {
			gamesLock.writeLock().lock();
			games.add(game);
		} finally {
			gamesLock.writeLock().unlock();
		}
	}

	public List<Game> getGames() {
		try {
			gamesLock.readLock().lock();
			return games;
		} finally {
			gamesLock.readLock().unlock();
		}
	}

	public List<Game> getGamesWithOnePlayer() {
		try {
			gamesLock.readLock().lock();
			return games.stream().filter(game -> !game.getGuest().isPresent()).collect(Collectors.toList());
		} finally {
			gamesLock.readLock().unlock();
		}
	}

	public Optional<Game> getGameById(final UUID id) {
		try {
			gamesLock.readLock().lock();
			return games.stream().filter(game -> game.getId().equals(id)).findFirst();
		} finally {
			gamesLock.readLock().unlock();
		}
	}

	public Optional<Game> getGameByUsername(final String name) {
		try {
			gamesLock.readLock().lock();
			return games.stream().filter(game -> game.getHost().getUsername().equals(name) || (game.getGuest()
				.isPresent() && game.getGuest().get().getUsername().equals(name))).findFirst();
		} finally {
			gamesLock.readLock().unlock();
		}
	}
}

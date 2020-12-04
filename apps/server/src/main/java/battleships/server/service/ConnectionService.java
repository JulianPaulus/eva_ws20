package battleships.server.service;

import battleships.net.connection.Connection;
import battleships.net.connection.ConnectionEvent;
import battleships.observable.Observable;
import battleships.observable.Observer;
import battleships.server.connection.AuthenticatedConnection;
import battleships.server.connection.GameConnection;
import battleships.server.game.Player;
import battleships.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConnectionService implements Observer<ConnectionEvent> {

	private static final ConnectionService INSTANCE = new ConnectionService();
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionService.class);

	private final List<Connection> unauthorizedConnections;
	private final Map<Player, AuthenticatedConnection> authorizedConnections;
	private final ReadWriteLock authorizedLock;
	private final ReadWriteLock unauthorizedLock;

	private ConnectionService() {
		this.unauthorizedConnections = new LinkedList<>();
		this.authorizedConnections = new HashMap<>();
		this.authorizedLock = new ReentrantReadWriteLock();
		this.unauthorizedLock = new ReentrantReadWriteLock();
	}

	public static ConnectionService getInstance() {
		return INSTANCE;
	}

	public void registerConnection(final Connection connection) {
		try {
			unauthorizedLock.writeLock().lock();
			if (!unauthorizedConnections.contains(connection)) {
				unauthorizedConnections.add(connection);
			}
		} finally {
			unauthorizedLock.writeLock().unlock();
			LOGGER.debug("unauthorizedConnections: {}", unauthorizedConnections.size());
			LOGGER.debug("authorizedConnections: {}", authorizedConnections.size());
		}
	}

	public void registerConnection(final AuthenticatedConnection connection) {
		try {
			unauthorizedLock.writeLock().lock();
			Optional<Connection> oldConnection = unauthorizedConnections.stream()
				.filter(con -> con.getUUID().equals(connection.getUUID())).findFirst();
			oldConnection.ifPresent(unauthorizedConnections::remove);
		} finally {
			unauthorizedLock.writeLock().unlock();
		}

		try {
			authorizedLock.writeLock().lock();
			authorizedConnections.put(connection.getPlayer(), connection);
		} finally {
			authorizedLock.writeLock().unlock();
			LOGGER.debug("unauthorizedConnections: {}", unauthorizedConnections.size());
			LOGGER.debug("authorizedConnections: {}", authorizedConnections.size());
			LOGGER.debug("GameConnection: {}", connection instanceof GameConnection);
		}
	}

	public void removeConnection(final Connection connection) {
		if (connection instanceof AuthenticatedConnection) {
			try {
				authorizedLock.writeLock().lock();
				authorizedConnections.remove(((AuthenticatedConnection) connection).getPlayer());
			} finally {
				authorizedLock.writeLock().unlock();
			}
		} else {
			try {
				unauthorizedLock.writeLock().lock();
				unauthorizedConnections.remove(connection);
			} finally {
				unauthorizedLock.writeLock().unlock();
			}
		}
	}

	public Optional<AuthenticatedConnection> getConnectionForPlayer(final Player player) {
		try {
			authorizedLock.readLock().lock();
			return Optional.ofNullable(authorizedConnections.get(player));
		} finally {
			authorizedLock.readLock().unlock();
		}
	}

	@Override
	public void update(final Observable<ConnectionEvent> object, final ConnectionEvent event) {
		if (event == ConnectionEvent.DISCONNECTED) {
			removeConnection((Connection) object);
		}
		LOGGER.debug("unauthorizedConnections: {}", unauthorizedConnections.size());
		LOGGER.debug("authorizedConnections: {}", authorizedConnections.size());
	}

	public boolean canAcceptConnections() {
		try {
			authorizedLock.readLock().lock();
			return authorizedConnections.size() <= Constants.Server.MAX_PLAYER_COUNT;
		} finally {
			authorizedLock.readLock().unlock();
		}
	}
}

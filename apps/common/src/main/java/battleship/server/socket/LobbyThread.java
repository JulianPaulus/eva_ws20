package battleship.server.socket;

import battleship.net.packet.AbstractPacket;
import battleship.util.AuthenticatedConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LobbyThread extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(LobbyThread.class);
	private static LobbyThread instance;

	private final List<AuthenticatedConnection> connections;
	private final Lock connectionsLock;

	private final List<AuthenticatedConnection> addConnections;
	private final Lock addLock;
	private final List<AuthenticatedConnection> removeConnections;
	private final Lock removeLock;

	private LobbyThread() {
		super("lobby");
		this.connections = new ArrayList<>();
		this.connectionsLock = new ReentrantLock();

		this.addConnections = new ArrayList<>();
		this.addLock = new ReentrantLock();

		this.removeConnections = new ArrayList<>();
		this.removeLock = new ReentrantLock();
	}

	@Override
	public void run() {
		logger.info("lobby starting...");
		while(!isInterrupted()) {
			connectionsLock.lock();
			try {
				for (AuthenticatedConnection connection : connections) {
					AbstractPacket packet = null;
					try {
						packet = connection.readPacket();
					} catch (InterruptedException e) {
						logger.trace("error in lobby thread", e);
					}
					if (packet != null) {
						handlePacket(connection, packet);
					}
				}

				removeLock.lock();
				try {
					connections.removeAll(removeConnections);
					removeConnections.clear();
				} finally {
					removeLock.unlock();
				}

				addLock.lock();
				try {
					connections.addAll(addConnections);
					addConnections.clear();
				} finally {
					addLock.unlock();
				}

			} finally {
				connectionsLock.unlock();
			}
		}
	}

	private void handlePacket(AuthenticatedConnection connection, AbstractPacket packet) {
		if (packet instanceof AbstractGeneralPacket) {
			AbstractGeneralPacket generalPacket = (AbstractGeneralPacket) packet;
			if (generalPacket.getConnectionSide().isServer()) {
				generalPacket.act(null, connection);
			} else {
				logger.warn("received client packet on server side from {}", connection);
			}
		} else {
			logger.warn("received unknown packet type from {}", connection);
		}
	}

	public void addConnection(AuthenticatedConnection connection) {
		addLock.lock();
		try {
			addConnections.add(connection);
		} finally {
			addLock.unlock();
		}
	}

	public void removeConnection(AuthenticatedConnection connection) {
		removeLock.lock();
		try {
			removeConnections.add(connection);
		} finally {
			removeLock.unlock();
		}
	}

	public synchronized static LobbyThread getInstance() {
		if (instance == null) {
			instance = new LobbyThread();
		}
		return instance;
	}
}

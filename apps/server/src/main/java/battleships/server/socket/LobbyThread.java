package battleships.server.socket;

import battleship.net.packet.AbstractGeneralPacket;
import battleship.net.packet.AbstractPacket;
import battleship.util.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LobbyThread extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(LobbyThread.class);
	private static LobbyThread instance;

	private final List<Connection> connections;
	private final Lock connectionsLock;

	private LobbyThread() {
		super("lobby");
		this.connections = new ArrayList<>();
		this.connectionsLock = new ReentrantLock();

	}

	@Override
	public void run() {
		logger.info("lobby starting...");
		while(!isInterrupted()) {
			connectionsLock.lock();
			try {
				for (Connection connection : connections) {
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
			} finally {
				connectionsLock.unlock();
			}
		}
	}

	private void handlePacket(Connection connection, AbstractPacket packet) {
		if (packet instanceof AbstractGeneralPacket) {
			AbstractGeneralPacket generalPacket = (AbstractGeneralPacket) packet;
			if (generalPacket.getConnectionSide().isServer()) {
				generalPacket.act(connection);
			} else {
				logger.warn("received client packet on server side from {}", connection);
			}
		} else {
			logger.warn("received unknown packet type from {}", connection);
		}
	}

	public void addConnection(Connection connection) {
		connectionsLock.lock();
		try {
			connections.add(connection);
		} finally {
			connectionsLock.unlock();
		}
	}

	public synchronized static LobbyThread getInstance() {
		if (instance == null) {
			instance = new LobbyThread();
		}
		return instance;
	}
}

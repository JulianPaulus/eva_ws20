package battleships.server.util;

import battleships.server.service.ConnectionService;
import battleships.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ConnectionManager extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);

	private final ConnectionService connectionService = ConnectionService.getInstance();

	public ConnectionManager() {
		setName("CM");
		setDaemon(true);
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			try {
				TimeUnit.MILLISECONDS.sleep(Constants.Server.CONNECTION_MANAGER_INTERVAL_MS);

				LOGGER.debug("ConnectionManager starting...");
				long startMS = System.currentTimeMillis();
				connectionService.closeStaleConnections();
				LOGGER.debug("ConnectionManager ran for {}ms", System.currentTimeMillis() - startMS);
			} catch (final InterruptedException e) {
				LOGGER.trace("ConnectionManager sleep interrupted", e);
			}
		}
	}
}

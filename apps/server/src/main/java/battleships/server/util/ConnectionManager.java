package battleships.server.util;

import battleships.server.service.ConnectionService;
import battleships.server.socket.ServerConfig;
import battleships.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ConnectionManager extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);

	private final ConnectionService connectionService = ConnectionService.getInstance();
	private long lastStaleConnectionTest = System.currentTimeMillis();
	private long lastHeartbeatSend = System.currentTimeMillis();

	public ConnectionManager() {
		setName("CM");
		setDaemon(true);
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			try {
				TimeUnit.SECONDS.sleep(1);
				if(System.currentTimeMillis() - lastHeartbeatSend >= TimeUnit.SECONDS.toMillis(Constants.Server.HEARTBEAT_REPEAT_TIME_IN_S)) {
					lastHeartbeatSend = System.currentTimeMillis();
					connectionService.sendHeartbeat();
				}
				if(System.currentTimeMillis() - lastStaleConnectionTest >= TimeUnit.SECONDS.toMillis(ServerConfig.getInstance().getConnectionManagerIntervalS())) {
					lastStaleConnectionTest = System.currentTimeMillis();
					long startMS = System.currentTimeMillis();
					connectionService.closeStaleConnections();
					LOGGER.debug("ConnectionManager ran for {}ms", System.currentTimeMillis() - startMS);
				}
			} catch (final InterruptedException e) {
				LOGGER.trace("ConnectionManager sleep interrupted", e);
			}
		}
	}
}

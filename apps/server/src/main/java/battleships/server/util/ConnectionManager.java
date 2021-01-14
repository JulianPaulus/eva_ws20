package battleships.server.util;

import battleships.server.service.ConnectionService;
import battleships.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ConnectionManager extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);

	private final ConnectionService connectionService = ConnectionService.getInstance();
	private long lastHeartbeatSend = System.currentTimeMillis();
	private long lastHeartbeatTimeoutCheck = System.currentTimeMillis();

	public ConnectionManager() {
		setName("CM");
		setDaemon(true);
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			try {
				if(System.currentTimeMillis() - lastHeartbeatSend >= TimeUnit.SECONDS.toMillis(Constants.HEARTBEAT_SEND_INTERVAL_IN_S)) {
					lastHeartbeatSend = System.currentTimeMillis();
					connectionService.sendHeartbeats();
				}
				if(System.currentTimeMillis() - lastHeartbeatTimeoutCheck >= TimeUnit.SECONDS.toMillis(Constants.HEARTBEAT_TIMEOUT_IN_S)) {
					lastHeartbeatTimeoutCheck = System.currentTimeMillis();
					connectionService.closeStaleConnections();
				}
				TimeUnit.SECONDS.sleep(1);
			} catch (final InterruptedException e) {
				LOGGER.trace("ConnectionManager sleep interrupted", e);
			}
		}
	}
}

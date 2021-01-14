package battleships.client.service;

import battleships.client.ClientMain;
import battleships.net.connection.Connection;
import battleships.net.packet.HeartbeatPacket;
import battleships.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class HeartbeatService extends Thread {
	private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatService.class);
	private static HeartbeatService instance;
	private long lastSentHeartbeat = System.currentTimeMillis();

	private HeartbeatService() {
		this.setDaemon(true);
	}

	public static HeartbeatService getAndStartInstance() {
		if (instance == null) {
			instance = new HeartbeatService();
			instance.start();
		}
		return instance;
	}

	@Override
	public void run() {
		super.run();
		while (!isInterrupted()) {
			if (ClientMain.getInstance().getConnection() != null
				&& !ClientMain.getInstance().getConnection().isClosed()) {
				Connection clientCon = ClientMain.getInstance().getConnection();
				if (System.currentTimeMillis() - lastSentHeartbeat > TimeUnit.SECONDS.toMillis(Constants.HEARTBEAT_SEND_INTERVAL_IN_S)) {
					clientCon.writePacket(new HeartbeatPacket());
					lastSentHeartbeat = System.currentTimeMillis();
				}
				if (System.currentTimeMillis() - clientCon.getLastHeartbeat() > TimeUnit.SECONDS.toMillis(Constants.HEARTBEAT_TIMEOUT_IN_S)) {
					clientCon.close();
				}
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (final InterruptedException e) {
				LOGGER.trace("Heartbeat sleep interrupted", e);
			}
		}
	}
}

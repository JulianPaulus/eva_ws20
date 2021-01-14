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

	private HeartbeatService() {

	}

	public static HeartbeatService getInstance() {
		if (instance == null) {
			instance = new HeartbeatService();
			instance.start();
		}
		return instance;
	}

	@Override
	public void run() {
		super.run();
		while (!isInterrupted() && ClientMain.getInstance().getConnection() != null
			&& !ClientMain.getInstance().getConnection().isClosed()) {
			Connection clientCon = ClientMain.getInstance().getConnection();
			try {
				clientCon.writePacket(new HeartbeatPacket());
				if (clientCon.getLastHeartbeat() < System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(Constants.HEARTBEAT_TIMEOUT_IN_S)) {
					clientCon.close();
				}
				TimeUnit.SECONDS.sleep(Math.min(Constants.HEARTBEAT_TIMEOUT_IN_S,
					Constants.HEARTBEAT_SEND_INTERVAL_IN_S));
			} catch (final InterruptedException e) {
				LOGGER.trace("Heartbeat sleep interrupted", e);
			}
		}
	}
}

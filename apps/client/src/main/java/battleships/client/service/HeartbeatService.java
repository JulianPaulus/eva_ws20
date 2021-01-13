package battleships.client.service;

import battleships.client.ClientMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class HeartbeatService extends Thread {
	private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatService.class);
	private static HeartbeatService instance;
	private long lastHeartbeat = System.currentTimeMillis();
	private static final long maxHeartbeatPause = TimeUnit.SECONDS.toMillis(10);

	private HeartbeatService() {

	}

	public static HeartbeatService getInstance() {
		if(instance == null) {
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
			try {
				TimeUnit.SECONDS.sleep(10);
				synchronized (this) {
					if(lastHeartbeat < System.currentTimeMillis() - maxHeartbeatPause
						&& ClientMain.getInstance().getConnection() != null) {
						ClientMain.getInstance().getConnection().close();
					}
				}
			} catch (final InterruptedException e) {
				LOGGER.trace("ConnectionManager sleep interrupted", e);
			}
		}
	}

	public synchronized void receiveHeartbeat() {
		lastHeartbeat = System.currentTimeMillis();
	}
}

package battleships.server.socket;

import battleships.util.Constants;

public class ServerConfig {

	private static final ServerConfig INSTANCE = new ServerConfig();

	private int port = Constants.Server.DEFAULT_PORT;
	private long connectionManagerIntervalMs = Constants.Server.CONNECTION_MANAGER_INTERVAL_MS;
	private long connectionTimeoutMs = Constants.Server.CONNECTION_TIMEOUT_MS;

	private ServerConfig() {}

	public static ServerConfig getInstance() {
		return INSTANCE;
	}

	public int getPort() {
		return port;
	}

	public void setPort(final int port) {
		this.port = port;
	}

	public long getConnectionManagerIntervalMs() {
		return connectionManagerIntervalMs;
	}

	public void setConnectionManagerIntervalMs(final long connectionManagerIntervalMs) {
		this.connectionManagerIntervalMs = connectionManagerIntervalMs;
	}

	public long getConnectionTimeoutMs() {
		return connectionTimeoutMs;
	}

	public void setConnectionTimeoutMs(final long connectionTimeoutMs) {
		this.connectionTimeoutMs = connectionTimeoutMs;
	}
}

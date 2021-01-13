package battleships.server.socket;

import battleships.util.Constants;

public class ServerConfig {

	private static final ServerConfig INSTANCE = new ServerConfig();

	private int port = Constants.Server.DEFAULT_PORT;
	private long connectionManagerIntervalS = Constants.Server.CONNECTION_MANAGER_INTERVAL_S;
	private long connectionTimeoutS = Constants.Server.CONNECTION_TIMEOUT_S;

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

	public long getConnectionManagerIntervalS() {
		return connectionManagerIntervalS;
	}

	public void setConnectionManagerIntervalS(final long connectionManagerIntervalS) {
		this.connectionManagerIntervalS = connectionManagerIntervalS;
	}

	public long getConnectionTimeoutS() {
		return connectionTimeoutS;
	}

	public void setConnectionTimeoutS(final long connectionTimeoutS) {
		this.connectionTimeoutS = connectionTimeoutS;
	}
}

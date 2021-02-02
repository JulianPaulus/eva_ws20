package battleships.server.socket;

import battleships.util.Constants;

public class ServerConfig {

	private static final ServerConfig INSTANCE = new ServerConfig();

	private int port = Constants.Server.DEFAULT_PORT;

	private boolean isNoEntropy = false;

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

	public boolean isNoEntropy() {
		return isNoEntropy;
	}

	public void setNoEntropy(boolean noEntropy) {
		isNoEntropy = noEntropy;
	}
}

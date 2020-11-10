package battleship.net;

public enum ConnectionSide {

	CLIENT,
	SERVER,
	BOTH;

	public boolean isClient() {
		return this == CLIENT || this == BOTH;
	}

	public boolean isServer() {
		return this == SERVER || this == BOTH;
	}

}

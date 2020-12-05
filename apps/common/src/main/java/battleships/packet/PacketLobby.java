package battleships.packet;

public class PacketLobby {
	private final int id;
	private final String name;

	public PacketLobby(final int id, final String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}

package battleships.model;

import java.util.UUID;

public class PacketLobby {
	private final UUID id;
	private final String name;

	public PacketLobby(final UUID id, final String name) {
		this.id = id;
		this.name = name;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}

package battleships.client.packet.receive;

import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;

import java.util.UUID;

public class GameJoinedPacket implements IPreAuthReceivePacket {

	public static final byte IDENTIFIER = Constants.Identifiers.GAME_JOIN_RESPONSE;

	private final UUID gameId;

	public GameJoinedPacket(final UUID gameId) {
		this.gameId = gameId;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(final Connection connection) {
		System.out.println("game join packet");
		System.out.println(gameId.toString());
	}
}

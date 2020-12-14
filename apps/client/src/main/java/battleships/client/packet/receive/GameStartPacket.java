package battleships.client.packet.receive;

import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;

public class GameStartPacket implements IPreAuthReceivePacket {

	public static final byte IDENTIFIER = Constants.Identifiers.START_GAME_MESSAGE;

	private final String otherPlayerName;

	public GameStartPacket(final String otherPlayerName) {
		this.otherPlayerName = otherPlayerName;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(final Connection connection) {
		System.out.println("start game");
		System.out.println("other player: " + otherPlayerName);
	}
}

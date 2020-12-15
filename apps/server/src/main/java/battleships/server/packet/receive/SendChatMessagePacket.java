package battleships.server.packet.receive;

import battleships.server.connection.GameConnection;
import battleships.server.packet.IGameReceivePacket;
import battleships.util.Constants;

public class SendChatMessagePacket implements IGameReceivePacket {

	public static final byte IDENTIFIER = Constants.Identifiers.SEND_CHAT_MESSAGE;

	private final String message;

	public SendChatMessagePacket(final String message) {
		this.message = message;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(final GameConnection connection) {
		connection.getGame().sendChatMessage(connection.getPlayer().getUsername(), message);
	}
}

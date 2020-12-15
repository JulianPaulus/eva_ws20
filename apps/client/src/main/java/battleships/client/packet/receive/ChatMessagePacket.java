package battleships.client.packet.receive;

import battleships.client.GameWindow.GameWindow;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;

public class ChatMessagePacket implements IPreAuthReceivePacket {

	public static final byte IDENTIFIER = Constants.Identifiers.BROADCAST_CHAT_MESSAGE;

	private final String fromUser;
	private final String message;

	public ChatMessagePacket(final String fromUser, final String message) {
		this.fromUser = fromUser;
		this.message = message;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(final Connection connection) {
		System.out.println(fromUser + ": " + message);
		GameWindow.getInstance().receiveMessage(fromUser, message);
	}
}

package battleships.client.packet.receive;

import battleships.client.game.GameWindow;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;
import javafx.application.Platform;

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
		Platform.runLater(() -> GameWindow.getInstance().receiveMessage(fromUser, message));
	}
}

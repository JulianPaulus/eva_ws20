package battleships.server.packet.send;

import battleships.net.packet.SendPacket;
import battleships.util.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class ChatMessagePacket extends SendPacket {
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
	protected void writeContent(final DataOutputStream dos) throws IOException {
		dos.writeUTF(fromUser);
		dos.writeUTF(message);
	}
}

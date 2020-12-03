package battleships.client.packet.receive;

import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;

public class ServerErrorPacket implements IPreAuthReceivePacket {

	public static final byte IDENTIFIER = Constants.Identifiers.SERVER_ERROR;
	private final String message;

	public ServerErrorPacket(final String message) {
		this.message = message;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(final Connection connection) {
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAH");
		System.out.println(message);
	}
}

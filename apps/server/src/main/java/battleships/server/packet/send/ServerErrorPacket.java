package battleships.server.packet.send;

import battleships.net.packet.SendPacket;
import battleships.util.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerErrorPacket extends SendPacket {

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
	protected DataOutputStream writeContent(final DataOutputStream dos) throws IOException {
		dos.writeUTF(message);
		return dos;
	}
}

package battleships.server.packet.send;

import battleships.net.packet.SendPacket;
import battleships.util.Constants;
import battleships.util.ServerErrorType;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerErrorPacket extends SendPacket {

	public static final byte IDENTIFIER = Constants.Identifiers.SERVER_ERROR;

	private final ServerErrorType type;
	private final String message;

	public ServerErrorPacket(final ServerErrorType type, final String message) {
		this.type = type;
		this.message = message;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected void writeContent(final DataOutputStream dos) throws IOException {
		dos.writeUTF(type.toString());
		dos.writeUTF(message);
	}
}

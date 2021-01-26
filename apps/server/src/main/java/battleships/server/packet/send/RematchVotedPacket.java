package battleships.server.packet.send;

import battleships.net.packet.SendPacket;
import battleships.util.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class RematchVotedPacket extends SendPacket {

	public static final byte IDENTIFIER = Constants.Identifiers.VOTED_REMATCH;
	private final String user;

	public RematchVotedPacket(final String user) {
		this.user = user;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected void writeContent(final DataOutputStream dos) throws IOException {
		dos.writeUTF(user);
	}

}

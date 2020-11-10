package battleship.net.packet;

import java.io.DataOutputStream;
import java.io.IOException;

public class LobbyListRequestPacket extends AbstractPacket {
	public static final byte IDENTIFIER = 0x2;

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected DataOutputStream writeContent(DataOutputStream dos) throws IOException {
		return dos;
	}
}

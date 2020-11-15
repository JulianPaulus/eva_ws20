package battleships.client.packet.send;

import battleships.Constants;
import battleships.net.packet.SendPacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class LobbyListRequestPacket extends SendPacket {
	public static final byte IDENTIFIER = Constants.Identifiers.LOBBY_LIST_REQUEST;

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected DataOutputStream writeContent(final DataOutputStream dos) throws IOException {
		return dos;
	}
}

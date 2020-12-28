package battleships.client.packet.send;

import battleships.net.packet.SendPacket;
import battleships.util.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class LobbyListRequestPacket extends SendPacket {
	public static final byte IDENTIFIER = Constants.Identifiers.LOBBY_LIST_REQUEST;

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected void writeContent(final DataOutputStream dos) {}
}

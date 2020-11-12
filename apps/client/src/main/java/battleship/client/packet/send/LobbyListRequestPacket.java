package battleship.client.packet.send;

import battleship.net.connection.AuthenticatedConnection;
import battleship.net.packet.AbstractLobbyPacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class LobbyListRequestPacket extends AbstractLobbyPacket {
	public static final byte IDENTIFIER = 0x2;

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected DataOutputStream writeContent(DataOutputStream dos) throws IOException {
		return dos;
	}

	@Override
	public void act(AuthenticatedConnection connection) {
		//Nothing
	}
}

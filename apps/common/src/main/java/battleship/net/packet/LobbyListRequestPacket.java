package battleship.net.packet;

import battleship.net.ConnectionSide;
import battleship.util.Connection;

import java.io.DataOutputStream;
import java.io.IOException;

public class LobbyListRequestPacket extends AbstractGeneralPacket {
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
	public void act(Connection connection) {

	}


	@Override
	public ConnectionSide getConnectionSide() {
		return ConnectionSide.SERVER;
	}
}

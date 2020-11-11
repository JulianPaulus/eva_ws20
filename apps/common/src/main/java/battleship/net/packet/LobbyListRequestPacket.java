package battleship.net.packet;

import battleship.net.ConnectionSide;
import battleship.packet.PacketLobby;
import refactor.AuthenticatedConnection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

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
		connection.writePacket(new LobbyListPacket(new HashSet<>(Arrays.asList(new PacketLobby(1, "Test1"), new PacketLobby(2, "Test2")))));
	}


	@Override
	public ConnectionSide getConnectionSide() {
		return ConnectionSide.SERVER;
	}
}

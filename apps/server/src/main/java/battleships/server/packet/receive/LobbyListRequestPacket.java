package battleships.server.packet.receive;

import battleship.net.connection.AuthenticatedConnection;
import battleship.net.packet.IReceivePacket;
import battleship.packet.PacketLobby;
import battleships.server.packet.send.LobbyListPacket;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class LobbyListRequestPacket implements IReceivePacket<AuthenticatedConnection> {
	public static final byte IDENTIFIER = 0x2;

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(AuthenticatedConnection connection) {
		try {
			connection.writePacket(new LobbyListPacket(new HashSet<>(Arrays.asList(new PacketLobby(1, "Test1"), new PacketLobby(2, "Test2")))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

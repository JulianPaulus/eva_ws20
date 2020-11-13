package battleships.server.packet.receive;

import battleships.net.connection.AuthenticatedConnection;
import battleships.net.packet.ILobbyReceivePacket;
import battleships.packet.PacketLobby;
import battleships.server.packet.send.LobbyListPacket;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class LobbyListRequestPacket implements ILobbyReceivePacket {
	public static final byte IDENTIFIER = 0x2;

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(final AuthenticatedConnection connection) {
		try {
			connection.writePacket(new LobbyListPacket(new HashSet<>(Arrays.asList(new PacketLobby(1, "Test1"), new PacketLobby(2, "Test2")))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

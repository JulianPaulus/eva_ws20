package battleship.net.connection.packethandler;

import battleship.net.connection.AuthenticatedConnection;
import battleship.net.packet.ILobbyReceivePacket;

public class LobbyPacketHandler extends AbstractPacketHandler<AuthenticatedConnection, ILobbyReceivePacket> {

	public LobbyPacketHandler() {
		super(AuthenticatedConnection.class, ILobbyReceivePacket.class);
	}

	@Override
	protected void handleImplementedPacketType(ILobbyReceivePacket packet, AuthenticatedConnection connection) {
		packet.act(connection);
	}
}

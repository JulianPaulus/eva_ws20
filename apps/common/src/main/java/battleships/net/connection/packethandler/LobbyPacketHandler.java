package battleships.net.connection.packethandler;

import battleships.net.connection.AuthenticatedConnection;
import battleships.net.packet.ILobbyReceivePacket;

public class LobbyPacketHandler extends AbstractPacketHandler<AuthenticatedConnection, ILobbyReceivePacket> {

	public LobbyPacketHandler() {
		super(AuthenticatedConnection.class, ILobbyReceivePacket.class);
	}

	@Override
	protected void handleImplementedPacketType(ILobbyReceivePacket packet, AuthenticatedConnection connection) {
		packet.act(connection);
	}
}

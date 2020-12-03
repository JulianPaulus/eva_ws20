package battleships.server.connection.packethandler;

import battleships.net.connection.packethandler.AbstractPacketHandler;
import battleships.server.connection.AuthenticatedConnection;
import battleships.server.packet.ILobbyReceivePacket;

public class LobbyPacketHandler extends AbstractPacketHandler<AuthenticatedConnection, ILobbyReceivePacket> {

	public LobbyPacketHandler() {
		super(AuthenticatedConnection.class, ILobbyReceivePacket.class);
	}

	@Override
	protected void handleImplementedPacketType(final ILobbyReceivePacket packet, final AuthenticatedConnection connection) {
		packet.act(connection);
	}
}

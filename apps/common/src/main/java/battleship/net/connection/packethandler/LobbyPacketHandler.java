package battleship.net.connection.packethandler;

import battleship.net.connection.AuthenticatedConnection;
import battleship.net.packet.IReceivePacketLobbyConnection;

public class LobbyPacketHandler extends AbstractPacketHandler<AuthenticatedConnection, IReceivePacketLobbyConnection> {

	public LobbyPacketHandler() {
		super(AuthenticatedConnection.class, IReceivePacketLobbyConnection.class);
	}

	@Override
	protected void handleImplementedPacketType(IReceivePacketLobbyConnection packet, AuthenticatedConnection connection) {
		packet.act(connection);
	}
}

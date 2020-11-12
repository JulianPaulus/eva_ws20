package battleship.net.connection.packethandler;

import battleship.net.connection.AuthenticatedConnection;

public class LobbyPacketHandler extends AbstractPacketHandler<AbstractLobbyPacket, AuthenticatedConnection> {

	public LobbyPacketHandler() {
		super(AbstractLobbyPacket.class, AuthenticatedConnection.class);
	}

	@Override
	protected void handleImplementedPacketType(AbstractLobbyPacket packet, AuthenticatedConnection connection) {
		packet.act(connection);
	}
}

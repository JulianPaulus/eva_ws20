package refactor.packethandler;

import battleship.net.packet.AbstractLobbyPacket;
import refactor.AuthenticatedConnection;

public class LobbyPacketHandler extends AbstractPacketHandler<AbstractLobbyPacket, AuthenticatedConnection> {

	public LobbyPacketHandler() {
		super(AbstractLobbyPacket.class, AuthenticatedConnection.class);
	}

	@Override
	protected void handleImplementedPacketType(AbstractLobbyPacket packet, AuthenticatedConnection connection) {
		packet.act(connection);
	}
}

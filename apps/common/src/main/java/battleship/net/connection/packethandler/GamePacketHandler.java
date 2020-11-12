package battleship.net.connection.packethandler;

import battleship.net.connection.GameConnection;
import battleship.net.packet.IReceivePacket;

public class GamePacketHandler extends AbstractPacketHandler<GameConnection> {

	public GamePacketHandler() {
		super( , GameConnection.class);
	}

	@Override
	protected void handleImplementedPacketType(IReceivePacket packet, GameConnection connection) {
		packet.act(connection);
	}
}

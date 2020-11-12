package battleship.net.connection.packethandler;

import battleship.net.connection.GameConnection;
import battleship.net.packet.IGameReceivePacket;

public class GamePacketHandler extends AbstractPacketHandler<GameConnection, IGameReceivePacket> {

	public GamePacketHandler() {
		super(GameConnection.class, IGameReceivePacket.class);
	}

	@Override
	protected void handleImplementedPacketType(IGameReceivePacket packet, GameConnection connection) {
		packet.act(connection);
	}
}

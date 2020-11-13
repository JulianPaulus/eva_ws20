package battleships.net.connection.packethandler;

import battleships.net.connection.GameConnection;
import battleships.net.packet.IGameReceivePacket;

public class GamePacketHandler extends AbstractPacketHandler<GameConnection, IGameReceivePacket> {

	public GamePacketHandler() {
		super(GameConnection.class, IGameReceivePacket.class);
	}

	@Override
	protected void handleImplementedPacketType(final IGameReceivePacket packet, final GameConnection connection) {
		packet.act(connection);
	}
}

package battleship.net.connection.packethandler;

import battleship.net.connection.GameConnection;
import battleship.net.packet.AbstractGamePacket;

public class GamePacketHandler extends AbstractPacketHandler<AbstractGamePacket, GameConnection> {

	public GamePacketHandler() {
		super(AbstractGamePacket.class, GameConnection.class);
	}

	@Override
	protected void handleImplementedPacketType(AbstractGamePacket packet, GameConnection connection) {
		packet.act(connection);
	}
}

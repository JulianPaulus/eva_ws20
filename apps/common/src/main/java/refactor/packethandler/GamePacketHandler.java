package refactor.packethandler;

import battleship.net.packet.AbstractGamePacket;
import refactor.GameConnection;

public class GamePacketHandler extends AbstractPacketHandler<AbstractGamePacket, GameConnection> {

	public GamePacketHandler() {
		super(AbstractGamePacket.class, GameConnection.class);
	}

	@Override
	protected void handleImplementedPacketType(AbstractGamePacket packet, GameConnection connection) {
		packet.act(connection);
	}
}

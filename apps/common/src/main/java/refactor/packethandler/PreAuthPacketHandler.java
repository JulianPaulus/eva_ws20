package refactor.packethandler;

import battleship.net.packet.AbstractPacket;
import refactor.Connection;

public class PreAuthPacketHandler extends AbstractPacketHandler<AbstractPacket, Connection> {

	public PreAuthPacketHandler() {
		super(AbstractPacket.class, Connection.class);
	}

	@Override
	protected void handleImplementedPacketType(AbstractPacket packet, Connection connection) {
		packet.act(connection);
	}
}

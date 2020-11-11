package battleship.net.connection.packethandler;

import battleship.net.connection.Connection;
import battleship.net.packet.AbstractPacket;

public class PreAuthPacketHandler extends AbstractPacketHandler<AbstractPacket, Connection> {

	public PreAuthPacketHandler() {
		super(AbstractPacket.class, Connection.class);
	}

	@Override
	protected void handleImplementedPacketType(AbstractPacket packet, Connection connection) {
		packet.act(connection);
	}
}

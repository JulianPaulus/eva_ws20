package battleship.net.connection.packethandler;

import battleship.net.connection.Connection;
import battleship.net.packet.IPacket;

public class PreAuthPacketHandler extends AbstractPacketHandler<IPacket, Connection> {

	public PreAuthPacketHandler() {
		super(IPacket.class, Connection.class);
	}

	@Override
	protected void handleImplementedPacketType(IPacket packet, Connection connection) {
		packet.act(connection);
	}
}

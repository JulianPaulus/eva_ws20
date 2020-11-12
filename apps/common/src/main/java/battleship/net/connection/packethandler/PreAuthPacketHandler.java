package battleship.net.connection.packethandler;

import battleship.net.connection.Connection;
import battleship.net.packet.IPreAuthReceivePacket;

public class PreAuthPacketHandler extends AbstractPacketHandler<Connection, IPreAuthReceivePacket> {

	public PreAuthPacketHandler() {
		super(Connection.class, IPreAuthReceivePacket.class);
	}

	@Override
	protected void handleImplementedPacketType(IPreAuthReceivePacket packet, Connection connection) {
		packet.act(connection);
	}
}

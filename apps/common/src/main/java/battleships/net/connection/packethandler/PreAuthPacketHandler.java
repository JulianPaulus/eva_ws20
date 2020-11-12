package battleships.net.connection.packethandler;

import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;

public class PreAuthPacketHandler extends AbstractPacketHandler<Connection, IPreAuthReceivePacket> {

	public PreAuthPacketHandler() {
		super(Connection.class, IPreAuthReceivePacket.class);
	}

	@Override
	protected void handleImplementedPacketType(IPreAuthReceivePacket packet, Connection connection) {
		packet.act(connection);
	}
}

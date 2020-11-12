package battleship.net.connection.packethandler;

import battleship.net.connection.Connection;
import battleship.net.packet.IPacket;
import battleship.net.packet.IReceivePacketConnection;
import battleship.net.packet.IReceivePacketGameConnection;

public class PreAuthPacketHandler extends AbstractPacketHandler<Connection, IReceivePacketConnection> {

	public PreAuthPacketHandler() {
		super(Connection.class, IReceivePacketConnection.class);
	}

	@Override
	protected void handleImplementedPacketType(IReceivePacketConnection packet, Connection connection) {
		packet.act(connection);
	}
}

package battleship.net.connection.packethandler;

import battleship.net.connection.GameConnection;
import battleship.net.packet.IReceivePacketGameConnection;

public class GamePacketHandler extends AbstractPacketHandler<GameConnection, IReceivePacketGameConnection> {

	public GamePacketHandler() {
		super(GameConnection.class, IReceivePacketGameConnection.class);
	}

	@Override
	protected void handleImplementedPacketType(IReceivePacketGameConnection packet, GameConnection connection) {
		packet.act(connection);
	}
}

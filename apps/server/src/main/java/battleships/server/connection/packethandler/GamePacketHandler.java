package battleships.server.connection.packethandler;

import battleships.net.connection.packethandler.AbstractPacketHandler;
import battleships.server.connection.GameConnection;
import battleships.server.packet.IGameReceivePacket;

public class GamePacketHandler extends AbstractPacketHandler<GameConnection, IGameReceivePacket> {

	public GamePacketHandler() {
		super(GameConnection.class, IGameReceivePacket.class);
	}

}

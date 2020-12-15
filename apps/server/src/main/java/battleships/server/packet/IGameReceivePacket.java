package battleships.server.packet;

import battleships.net.packet.IReceivePacket;
import battleships.server.connection.GameConnection;

public interface IGameReceivePacket extends IReceivePacket<GameConnection> {
}

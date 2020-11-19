package battleship.net.packet;

import battleship.net.connection.Connection;

public interface IReceivePacket<ConnectionType extends Connection> extends IPacket {

	void act(ConnectionType connection);

}

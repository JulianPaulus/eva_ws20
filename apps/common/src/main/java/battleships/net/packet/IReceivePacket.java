package battleships.net.packet;

import battleships.net.connection.Connection;

public interface IReceivePacket<ConnectionType extends Connection> extends IPacket {

	void act(ConnectionType connection);

}

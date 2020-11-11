package battleship.net.packet;

import battleship.net.ConnectionSide;
import battleship.util.Connection;

public abstract class AbstractGeneralPacket<T> extends AbstractPacket {

	public abstract void act(T t, Connection connection);

	public abstract ConnectionSide getConnectionSide();

}

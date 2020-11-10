package battleship.net.packet;

import battleship.net.ConnectionSide;
import battleship.util.Connection;

public abstract class AbstractGeneralPacket extends AbstractPacket {

	public abstract void act(Connection connection);

	public abstract ConnectionSide getConnectionSide();

}

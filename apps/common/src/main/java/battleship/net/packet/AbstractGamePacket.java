package battleship.net.packet;

import battleship.packet.Game;
import battleship.util.Connection;

public abstract class AbstractGamePacket extends AbstractPacket {

	public abstract void act(Game game, Connection connection);

}

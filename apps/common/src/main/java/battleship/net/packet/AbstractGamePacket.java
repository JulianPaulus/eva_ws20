package battleship.net.packet;

import battleship.packet.Game;
import battleship.util.GameConnection;

public abstract class AbstractGamePacket extends AbstractPacket {

	public abstract void act(Game game, GameConnection connection);

}

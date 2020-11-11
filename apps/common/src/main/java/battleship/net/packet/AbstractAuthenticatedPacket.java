package battleship.net.packet;

import battleship.packet.Game;
import battleship.util.AuthenticatedConnection;

public abstract class AbstractAuthenticatedPacket extends AbstractPacket {

	public abstract void act(Game game, AuthenticatedConnection connection);

}

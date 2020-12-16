package battleships.client.packet.receive;

import battleships.client.GameWindow.GameWindow;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;

public class GameEnemiesTurnPacket implements IPreAuthReceivePacket {
	public static final byte IDENTIFIER = Constants.Identifiers.GAME_ENEMIES_TURN;

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(Connection connection) {
		GameWindow.getInstance().onEnemyTurn();
	}
}

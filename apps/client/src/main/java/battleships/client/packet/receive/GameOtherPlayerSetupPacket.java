package battleships.client.packet.receive;

import battleships.client.GameWindow.GameWindow;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;

public class GameOtherPlayerSetupPacket implements IPreAuthReceivePacket {
	public static final byte IDENTIFIER = Constants.Identifiers.GAME_WAIT_FOR_OTHER_PLAYER_SETUP_MESSAGE;

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(Connection connection) {
		GameWindow.getInstance().onWaitForOtherPlayerSetup();
	}
}

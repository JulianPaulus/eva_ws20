package battleships.client.packet.receive;

import battleships.client.ClientMain;
import battleships.client.GameWindow.GameWindow;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;

public class GamePlayerDoSetupPacket implements IPreAuthReceivePacket {

	public static final byte IDENTIFIER = Constants.Identifiers.GAME_PLAYER_DO_SETUP_MESSAGE;

	private final String otherPlayerName;

	public GamePlayerDoSetupPacket(final String otherPlayerName) {
		this.otherPlayerName = otherPlayerName;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(final Connection connection) {
		System.out.println("start game");
		System.out.println("other player: " + otherPlayerName);
		GameWindow.getInstance().onDoSetup();
	}
}

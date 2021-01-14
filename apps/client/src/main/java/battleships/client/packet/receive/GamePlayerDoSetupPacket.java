package battleships.client.packet.receive;

import battleships.client.game.GameWindow;
import battleships.client.game.StatusMessageType;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;
import javafx.application.Platform;

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
		GameWindow.getInstance().displayStatusMessage(otherPlayerName + " ist beigetreten", StatusMessageType.INFO);
		Platform.runLater(() -> GameWindow.getInstance().onDoSetup(otherPlayerName));
	}
}

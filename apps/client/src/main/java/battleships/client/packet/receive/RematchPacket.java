package battleships.client.packet.receive;

import battleships.client.game.GameWindow;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;
import javafx.application.Platform;

public class RematchPacket implements IPreAuthReceivePacket {

	public static final byte IDENTIFIER = Constants.Identifiers.START_REMATCH;

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(Connection connection) {
		Platform.runLater(() -> GameWindow.getInstance().rematch());
	}
}

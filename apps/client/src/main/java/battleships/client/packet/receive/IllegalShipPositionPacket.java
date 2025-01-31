package battleships.client.packet.receive;

import battleships.client.ClientMain;
import battleships.client.game.GameWindow;
import battleships.client.util.ErrorDialog;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;
import javafx.application.Platform;

public class IllegalShipPositionPacket implements IPreAuthReceivePacket {
	public static final byte IDENTIFIER = Constants.Identifiers.GAME_PLAYER_ILLEGAL_SHIP_POSITION;

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(Connection connection) {
		Platform.runLater(() -> {
			GameWindow.getInstance().removeAllShips();
			ErrorDialog.show(ClientMain.getInstance().getStage(), "Schiffe an illegalen Positionen festgestellt!");
		});
	}
}

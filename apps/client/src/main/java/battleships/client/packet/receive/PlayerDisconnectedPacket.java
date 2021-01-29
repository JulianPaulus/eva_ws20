package battleships.client.packet.receive;

import battleships.client.ClientMain;
import battleships.client.game.GameWindow;
import battleships.client.util.ClientState;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;
import javafx.application.Platform;

public class PlayerDisconnectedPacket implements IPreAuthReceivePacket {

	public static final byte IDENTIFIER = Constants.Identifiers.PLAYER_DISCONNECTED;

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(final Connection connection) {
		Platform.runLater(() -> {
			if (ClientMain.getInstance().getState() == ClientState.IN_GAME) {
				GameWindow.getInstance().onPlayerDisconnected();
			}
		});
	}
}

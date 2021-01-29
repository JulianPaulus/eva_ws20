package battleships.client.packet.receive;

import battleships.client.ClientMain;
import battleships.client.game.GameWindow;
import battleships.client.game.StatusMessageType;
import battleships.client.util.ClientState;
import battleships.client.util.ErrorDialog;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;
import battleships.util.ServerErrorType;
import javafx.application.Platform;

public class ServerErrorPacket implements IPreAuthReceivePacket {

	public static final byte IDENTIFIER = Constants.Identifiers.SERVER_ERROR;

	private final ServerErrorType type;
	private final String message;

	public ServerErrorPacket(final ServerErrorType type, final String message) {
		this.type = type;
		this.message = message;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(final Connection connection) {
		Platform.runLater(() -> {
			if (ClientMain.getInstance().getState() == ClientState.IN_GAME) {
				GameWindow.getInstance().displayStatusMessage(message, StatusMessageType.CRITICAL);
			} else {
				ErrorDialog.show(ClientMain.getInstance().getStage(), message);
			}
		});
	}
}

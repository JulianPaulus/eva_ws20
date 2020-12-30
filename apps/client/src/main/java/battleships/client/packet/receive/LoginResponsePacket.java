package battleships.client.packet.receive;

import battleships.client.ClientMain;
import battleships.client.lobby.LobbyListController;
import battleships.client.util.ClientState;
import battleships.client.util.ErrorDialog;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;

public class LoginResponsePacket implements IPreAuthReceivePacket {

	public static final byte IDENTIFIER = Constants.Identifiers.LOGIN_RESPONSE;

	private final int playerId;
	private final boolean successful;

	public LoginResponsePacket(final int playerId, final boolean successful) {
		this.playerId = playerId;
		this.successful = successful;
	}

	@Override
	public void act(final Connection connection) {
		if (successful) {
			ClientMain.getInstance().setState(ClientState.LOGGED_IN);
			LobbyListController.openWindow(ClientMain.getInstance().getStage());
		} else {
			ErrorDialog.show(ClientMain.getInstance().getStage(), "Login fehlgeschlagen!");
		}
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	public int getPlayerId() {
		return playerId;
	}

	public boolean isSuccessful() {
		return successful;
	}
}

package battleships.client.packet.receive;

import battleships.client.ClientMain;
import battleships.client.game.GameWindow;
import battleships.client.util.ClientState;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;

public class PlayerDisconnectedPacket implements IPreAuthReceivePacket {

	public static final byte IDENTIFIER = Constants.Identifiers.PLAYER_DISCONNECTED;

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(final Connection connection) {
		if (ClientMain.getInstance().getState() == ClientState.IN_GAME) {
			GameWindow.getInstance().onPlayerDisconnected();
		}
	}
}

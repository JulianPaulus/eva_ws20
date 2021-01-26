package battleships.client.packet.receive;

import battleships.client.game.GameWindow;
import battleships.client.game.StatusMessageType;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;

public class VotedRematchPacket implements IPreAuthReceivePacket {

	public static final byte IDENTIFIER = Constants.Identifiers.VOTED_REMATCH;
	private final String user;

	public VotedRematchPacket(final String user) {
		this.user = user;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(Connection connection) {
		GameWindow.getInstance().displayStatusMessage(user + " hat f\u01D6r ein Rematch gestimmt!", StatusMessageType.CRITICAL);
	}
}

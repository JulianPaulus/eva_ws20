package battleships.server.packet.receive;

import battleships.server.connection.GameConnection;
import battleships.server.packet.IGameReceivePacket;
import battleships.util.Constants;

public class VoteRematchPacket implements IGameReceivePacket {

	public static final byte IDENTIFIER = Constants.Identifiers.VOTE_REMATCH;

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(final GameConnection connection) {
		connection.getGame().voteRematch(connection.getPlayer());
	}
}

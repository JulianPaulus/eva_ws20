package battleships.server.packet.receive;

import battleships.server.connection.GameConnection;
import battleships.server.packet.IGameReceivePacket;
import battleships.util.Constants;

public class ShootPacket implements IGameReceivePacket {
	public static final byte IDENTIFIER = Constants.Identifiers.GAME_SHOOT_REQUEST;

	private int xPos;
	private int yPos;

	public ShootPacket(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(GameConnection connection) {
		connection.getGame().shoot(connection.getPlayer().getId(), xPos, yPos);
	}
}

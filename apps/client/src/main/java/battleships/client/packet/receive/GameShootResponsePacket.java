package battleships.client.packet.receive;

import battleships.client.GameWindow.GameWindow;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;

public class GameShootResponsePacket implements IPreAuthReceivePacket {
	public static final byte IDENTIFIER = Constants.Identifiers.GAME_SHOOT_RESPONSE;

	private final boolean isPlayerField;
	private final boolean isHit;
	private final boolean isGameEnd;
	private final int xPos;
	private final int yPos;

	public GameShootResponsePacket(boolean isPlayerField, boolean isHit,
								   boolean isGameEnd, int xPos, int yPos) {
		this.isPlayerField = isPlayerField;
		this.isHit = isHit;
		this.isGameEnd = isGameEnd;
		this.xPos = xPos;
		this.yPos = yPos;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(Connection connection) {
		GameWindow.getInstance().setHitOrMiss(isPlayerField, isHit, xPos, yPos);
		if(isGameEnd) {
			GameWindow.getInstance().setGameEnd(!isPlayerField);
		}
	}
}

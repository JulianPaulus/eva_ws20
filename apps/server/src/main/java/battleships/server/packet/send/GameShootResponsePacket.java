package battleships.server.packet.send;

import battleships.net.packet.SendPacket;
import battleships.util.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class GameShootResponsePacket extends SendPacket {
	public static final byte IDENTIFIER = Constants.Identifiers.GAME_SHOOT_RESPONSE;

	private final boolean isPlayerField;
	private final boolean isHit;
	private final boolean isDestroyed;
	private final boolean isGameEnd;
	private final int xPos;
	private final int yPos;

	public GameShootResponsePacket(boolean isPlayerField, boolean isHit, boolean isDestroyed,
								   boolean isGameEnd, int xPos, int yPos) {
		this.isPlayerField = isPlayerField;
		this.isHit = isHit;
		this.isDestroyed = isDestroyed;
		this.isGameEnd = isGameEnd;
		this.xPos = xPos;
		this.yPos = yPos;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected DataOutputStream writeContent(DataOutputStream dos) throws IOException {
		dos.writeBoolean(isPlayerField);
		dos.writeBoolean(isHit);
		dos.writeBoolean(isDestroyed);
		dos.writeBoolean(isGameEnd);
		dos.writeInt(xPos);
		dos.writeInt(yPos);
		return dos;
	}
}

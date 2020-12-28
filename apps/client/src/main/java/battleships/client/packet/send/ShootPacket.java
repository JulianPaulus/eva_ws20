package battleships.client.packet.send;

import battleships.net.packet.SendPacket;
import battleships.util.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class ShootPacket extends SendPacket {
	public static final byte IDENTIFIER = Constants.Identifiers.GAME_SHOOT_REQUEST;

	private int xPos;
	private int yPos;

	public ShootPacket(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
	}

	public int getxPos() {
		return xPos;
	}

	public int getyPos() {
		return yPos;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected void writeContent(DataOutputStream dos) throws IOException {
		dos.writeInt(xPos);
		dos.writeInt(yPos);
	}
}

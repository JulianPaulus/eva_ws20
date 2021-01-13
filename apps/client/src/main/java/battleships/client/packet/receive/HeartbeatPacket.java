package battleships.client.packet.receive;

import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;


public class HeartbeatPacket implements IPreAuthReceivePacket {
	public static final byte IDENTIFIER = Constants.Identifiers.HEARTBEAT;

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(Connection connection) {
		System.out.println("Heartbeat");
	}
}

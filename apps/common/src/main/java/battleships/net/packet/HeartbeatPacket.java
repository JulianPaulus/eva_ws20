package battleships.net.packet;

import battleships.net.connection.Connection;
import battleships.util.Constants;

import java.io.DataOutputStream;
import java.io.IOException;


public class HeartbeatPacket extends SendPacket implements IReceivePacket<Connection> {
	public static final byte IDENTIFIER = Constants.Identifiers.HEARTBEAT;

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected void writeContent(DataOutputStream dos) throws IOException {}

	@Override
	public void act(Connection connection) {
		connection.updateHeartbeat();
	}
}

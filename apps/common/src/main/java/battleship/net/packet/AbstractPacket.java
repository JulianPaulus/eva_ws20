package battleship.net.packet;

import battleship.net.ConnectionSide;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class AbstractPacket<ConnectionType extends battleship.net.connection.Connection> {

	public byte[] marshal() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		dos.writeByte(getIdentifier());
		writeContent(dos);
		return bos.toByteArray();
	}

	public abstract byte getIdentifier();

	protected abstract DataOutputStream writeContent(DataOutputStream dos) throws IOException;

	public abstract ConnectionSide getConnectionSide();

	public abstract void act(ConnectionType connection);

}

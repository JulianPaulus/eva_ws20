package battleship.net.packet;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class SendPacket implements IPacket {

	public byte[] marshal() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		dos.writeByte(getIdentifier());
		writeContent(dos);
		return bos.toByteArray();
	}

	protected abstract DataOutputStream writeContent(DataOutputStream dos) throws IOException;

}

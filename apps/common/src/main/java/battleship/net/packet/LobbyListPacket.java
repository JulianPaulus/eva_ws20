package battleship.net.packet;

import java.io.*;
import java.util.Set;

public class LobbyListPacket extends AbstractPacket {

	public static final byte IDENTIFIER = 0x1;
	private Set<String> lobbySet;

	public LobbyListPacket(Set<String> lobbySet) {
		this.lobbySet = lobbySet;
	}

	@Override
	public byte[] marshal() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		dos.writeByte(IDENTIFIER);

		for (String lobby : lobbySet) {
			dos.writeUTF(lobby);




		}

		return bos.toByteArray();
	}
}

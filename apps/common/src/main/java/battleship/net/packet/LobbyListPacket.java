package battleship.net.packet;

import java.io.*;
import java.util.Set;

public class LobbyListPacket extends AbstractPacket {

	public static final byte IDENTIFIER = 0x1;
	private final Set<String> lobbySet;

	public LobbyListPacket(Set<String> lobbySet) {
		this.lobbySet = lobbySet;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected DataOutputStream writeContent(DataOutputStream dos) throws IOException {
		for (String lobby : lobbySet) {
			dos.writeShort(lobbySet.size());
			dos.writeUTF(lobby);
		}
		return dos;
	}
}

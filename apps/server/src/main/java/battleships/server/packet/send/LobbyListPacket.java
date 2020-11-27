package battleships.server.packet.send;

import battleships.net.packet.SendPacket;
import battleships.packet.Game;
import battleships.util.Constants;
import battleships.util.Utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

public class LobbyListPacket extends SendPacket {

	public static final byte IDENTIFIER = Constants.Identifiers.LOBBY_LIST_RESPONSE;
	private final Collection<Game> games;

	public LobbyListPacket(final Collection<Game> lobbySet) {
		this.games = lobbySet;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected DataOutputStream writeContent(final DataOutputStream dos) throws IOException {
		dos.writeShort(games.size());
		for (final Game game : games) {
			writeUUID(dos, game.getId());
			dos.writeUTF(game.getHost().getUsername());
		}
		return dos;
	}

	private void writeUUID(final DataOutputStream dos, final UUID uuid) throws IOException {
		byte[] idBytes = Utils.getBytesFromUUID(uuid);
		for (int i = 0; i < Constants.UUID_BYTE_COUNT; i++) {
			dos.writeByte(idBytes[i]);
		}
	}
}

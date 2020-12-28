package battleships.server.packet.send;

import battleships.net.packet.SendPacket;
import battleships.server.game.Game;
import battleships.util.Constants;
import battleships.util.Utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

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
	protected void writeContent(final DataOutputStream dos) throws IOException {
		dos.writeShort(games.size());
		for (final Game game : games) {
			Utils.writeUUIDToStream(dos, game.getId());
			dos.writeUTF(game.getHost().getUsername());
		}
	}
}

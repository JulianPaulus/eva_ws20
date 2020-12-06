package battleships.server.game;

import battleships.net.connection.ConnectionEvent;
import battleships.net.packet.SendPacket;
import battleships.observable.Observable;
import battleships.observable.Observer;
import battleships.server.connection.AuthenticatedConnection;
import battleships.server.exception.ServerException;
import battleships.server.packet.send.ChatMessagePacket;
import battleships.server.packet.send.ServerErrorPacket;
import battleships.server.service.ConnectionService;
import battleships.server.service.GameService;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

// TODO threadsafety
public class Game implements Observer<ConnectionEvent> {

	private static final ConnectionService CONNECTION_SERVICE = ConnectionService.getInstance();
	private static final GameService GAME_SERVICE = GameService.getInstance();

	private final UUID id;

	private final Player host;
	private Player guest;

	// replace with something like GameState
	private boolean initialized = false;

	private AuthenticatedConnection hostConnection;
	private AuthenticatedConnection guestConnection;

	public Game(final Player host) {
		this.id = UUID.randomUUID();
		this.host = host;
	}

	public void addGuest(final Player guest) throws ServerException {
		if (this.guest == null) {
			this.guest = guest;

			hostConnection = loadConnection(host);
			guestConnection = loadConnection(guest);
			initialized = true;

			// TODO send join message to host
		} else {
			throw new ServerException("Game is full!");
		}
	}

	public UUID getId() {
		return id;
	}

	public Player getHost() {
		return host;
	}

	public Optional<Player> getGuest() {
		return Optional.ofNullable(guest);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Game game = (Game) o;
		return id.equals(game.id) && host.equals(game.host) && Objects.equals(guest, game.guest);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, host, guest);
	}

	@Override
	public void update(final Observable<ConnectionEvent> connection, final ConnectionEvent event) {
		if (event == ConnectionEvent.DISCONNECTED) {
			onPlayerDisconnected((AuthenticatedConnection) connection);
		}
	}

	private void onPlayerDisconnected(final AuthenticatedConnection cause) {
		if (initialized) {
			if (cause.getPlayer().equals(host)) {
				guestConnection.writePacket(new ServerErrorPacket(host.getUsername() + " disconnected!"));
			} else {
				hostConnection.writePacket(new ServerErrorPacket(guest.getUsername() + " disconnected!"));
			}
		}
		GAME_SERVICE.removeGame(this);
	}

	public void sendChatMessage(final String fromUser, final String message) {
		broadcastPacket(new ChatMessagePacket(fromUser, message));
	}

	private void broadcastPacket(final SendPacket packet) {
		hostConnection.writePacket(packet);
		guestConnection.writePacket(packet);
	}

	private static AuthenticatedConnection loadConnection(final Player player) {
		return CONNECTION_SERVICE.getConnectionForPlayer(player).orElseThrow(() -> new ServerException("unable to load connection for " + player.getUsername()));
	}
}

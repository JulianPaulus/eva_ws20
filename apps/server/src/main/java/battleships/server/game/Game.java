package battleships.server.game;

import battleships.net.connection.ConnectionEvent;
import battleships.net.packet.SendPacket;
import battleships.observable.Observable;
import battleships.observable.Observer;
import battleships.server.connection.AuthenticatedConnection;
import battleships.server.exception.ServerException;
import battleships.server.game.gameState.ServerGameState;
import battleships.server.game.gameState.SettingUpState;
import battleships.server.game.gameState.UninitializedState;
import battleships.server.game.gameState.WaitingForGuestState;
import battleships.server.packet.send.ChatMessagePacket;
import battleships.server.packet.send.GameStartPacket;
import battleships.server.packet.send.ServerErrorPacket;
import battleships.server.service.ConnectionService;
import battleships.server.service.GameService;
import battleships.util.ServerErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Game implements Observer<ConnectionEvent> {

	private static final ConnectionService CONNECTION_SERVICE = ConnectionService.getInstance();
	private static final GameService GAME_SERVICE = GameService.getInstance();
	private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);

	private final UUID id;

	private final Player host;
	private Player guest;
	private ServerGameState state;

	private AuthenticatedConnection hostConnection;
	private AuthenticatedConnection guestConnection;

	public Game(final Player host) {
		this.id = UUID.randomUUID();
		this.host = host;
		this.state = new WaitingForGuestState();
	}

	public synchronized void addGuest(final Player guest) throws ServerException {
		if (this.guest == null && state.isWaitingForGuest()) {
			this.guest = guest;

			hostConnection = loadConnection(host);
			guestConnection = loadConnection(guest);
			hostConnection.writePacket(new GameStartPacket(guest.getUsername()));
			guestConnection.writePacket(new GameStartPacket(host.getUsername()));
			setState(new SettingUpState());
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

	public synchronized Optional<Player> getGuest() {
		return Optional.ofNullable(guest);
	}

	@Override
	public void update(final Observable<ConnectionEvent> connection, final ConnectionEvent event) {
		if (event == ConnectionEvent.DISCONNECTED) {
			onPlayerDisconnected((AuthenticatedConnection) connection);
		}
	}

	private synchronized void onPlayerDisconnected(final AuthenticatedConnection cause) {
		if (state.isInitialized()) {
			if (cause.getPlayer().equals(host)) {
				guestConnection.writePacket(new ServerErrorPacket(ServerErrorType.CRITICAL, host.getUsername() + " disconnected!"));
			} else {
				hostConnection.writePacket(new ServerErrorPacket(ServerErrorType.CRITICAL,guest.getUsername() + " disconnected!"));
			}
			setState(new UninitializedState());
		}
		GAME_SERVICE.removeGame(this);
	}

	public void sendChatMessage(final String fromUser, final String message) {
		broadcastPacket(new ChatMessagePacket(fromUser, message));
	}

	private synchronized void broadcastPacket(final SendPacket packet) {
		if (state.isInitialized()) {
			hostConnection.writePacket(packet);
			guestConnection.writePacket(packet);
		}
	}

	private synchronized void setState(final ServerGameState newState) {
		if (state.canTransition(newState)) {
			LOGGER.debug("[{}] updating state from {} to {}", id.toString(), state.getClass().getSimpleName(), newState.getClass().getSimpleName());
			state = newState;
		} else {
			LOGGER.warn("[{}] tried to transition state from {} to {}, which is forbidden", id.toString(), state.getClass().getSimpleName(), newState.getClass().getSimpleName());
		}
	}

	private static AuthenticatedConnection loadConnection(final Player player) {
		return CONNECTION_SERVICE.getConnectionForPlayer(player).orElseThrow(() -> new ServerException("unable to load connection for " + player.getUsername()));
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Game game = (Game) o;
		return id.equals(game.id) && host.equals(game.host) && Objects.equals(guest, game.guest) && state
			.equals(game.state) && hostConnection.equals(game.hostConnection) && Objects
			.equals(guestConnection, game.guestConnection);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, host, guest, state, hostConnection, guestConnection);
	}

	public ServerGameState getState() {
		return state;
	}
}

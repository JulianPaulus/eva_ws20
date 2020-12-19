package battleships.server.game;

import battleships.model.CoordinateState;
import battleships.model.Ship;
import battleships.model.ShipType;
import battleships.net.connection.Connection;
import battleships.net.connection.ConnectionEvent;
import battleships.net.exception.IllegalShipPositionException;
import battleships.net.packet.SendPacket;
import battleships.observable.Observable;
import battleships.observable.Observer;
import battleships.server.connection.AuthenticatedConnection;
import battleships.server.connection.GameConnection;
import battleships.server.exception.ServerException;
import battleships.server.game.gameState.GuestsTurnState;
import battleships.server.game.gameState.HostsTurnState;
import battleships.server.game.gameState.ServerGameState;
import battleships.server.game.gameState.SettingUpGuestState;
import battleships.server.game.gameState.SettingUpHostState;
import battleships.server.game.gameState.SettingUpState;
import battleships.server.game.gameState.UninitializedState;
import battleships.server.game.gameState.WaitingForGuestState;
import battleships.server.packet.send.ChatMessagePacket;
import battleships.server.packet.send.GameEnemiesTurnPacket;
import battleships.server.packet.send.GameJoinedPacket;
import battleships.server.packet.send.GamePlayerDoSetupPacket;
import battleships.server.packet.send.GamePlayersTurnPacket;
import battleships.server.packet.send.GameShootResponsePacket;
import battleships.server.packet.send.GameWaitForOtherPlayerSetupPacket;
import battleships.server.packet.send.ServerErrorPacket;
import battleships.server.service.ConnectionService;
import battleships.server.service.GameService;
import battleships.util.ServerErrorType;
import battleships.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class Game implements Observer<ConnectionEvent> {
	private static final int GAMEFIELD_SIZE = 10;

	private static final ConnectionService CONNECTION_SERVICE = ConnectionService.getInstance();
	private static final GameService GAME_SERVICE = GameService.getInstance();
	private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);

	private final UUID id;

	private final Player host;
	private Player guest;
	private CoordinateState[][] hostField;
	private CoordinateState[][] guestField;

	private ServerGameState state;

	private AuthenticatedConnection hostConnection;
	private AuthenticatedConnection guestConnection;

	public Game(final Player host) {
		this.id = UUID.randomUUID();
		this.host = host;
		this.state = new WaitingForGuestState();
	}

	public synchronized void setShips(final int playerId, Ship[] ships) {
		if(ships.length != 5) {
			throw new IllegalShipPositionException("Illegal number of ships");
		}
		Map<ShipType, Integer> requiredShips = Arrays
			.stream(ShipType.values()).collect(Collectors.toMap(x -> x, ShipType::getNrPerField));

		for(Ship ship : ships) {
			int reqNumber = requiredShips.get(ship.getType()) - 1;
			if(reqNumber < 0) {
				throw new IllegalShipPositionException("Too many ships of the same type!");
			}
			requiredShips.put(ship.getType(), reqNumber);
		}
		if(requiredShips.values().stream().anyMatch(x -> x != 0)) {
			throw new IllegalShipPositionException("Not enough ships!");
		}
		CoordinateState[][] gameField = new CoordinateState[GAMEFIELD_SIZE][GAMEFIELD_SIZE];
		for (int i = 0; i < GAMEFIELD_SIZE; i++) {
			Arrays.fill(gameField[i], CoordinateState.EMPTY);
		}
		for(Ship ship : ships) {
			Utils.validateShip(ship, gameField);
			//Place Ship
			for(int i = ship.getXCoordinate(); i <= ship.getEndXCoordinate(); i++) {
				for(int j = ship.getYCoordinate(); j <= ship.getEndYCoordinate(); j++) {
					gameField[i][j] = CoordinateState.SHIP;
				}
			}
		}
		if(playerId == host.getId() && this.state.canHostSetShip()) {
			this.hostField = gameField;
		} else if(guest != null && guest.getId() == playerId && this.state.canGuestSetShip()) {
			this.guestField = gameField;
		} else {
			throw new ServerException("Player is part of the game or is not allowed to set his ships in this state of the game!");
		}

		if(this.hostField != null && this.guestField != null) {
			setState(new HostsTurnState());
			hostConnection.writePacket(new GamePlayersTurnPacket());
			guestConnection.writePacket(new GameEnemiesTurnPacket());

		} else if(this.hostField != null) {
			setState(new SettingUpGuestState());
			hostConnection.writePacket(new GameWaitForOtherPlayerSetupPacket());

		} else if(this.guestField != null) {
			setState(new SettingUpHostState());
			guestConnection.writePacket(new GameWaitForOtherPlayerSetupPacket());

		}
	}

	public synchronized void shoot(final int playerId, int xPos, int yPos) {
		CoordinateState[][] targetField;
		Connection playerConnection;
		Connection targetConnection;
		boolean isHostsTurn;
		if(playerId == host.getId() && state.canHostFire()) {
			isHostsTurn = true;
			targetField = guestField;
			playerConnection = hostConnection;
			targetConnection = guestConnection;
		} else if(playerId == guest.getId() && state.canGuestFire()) {
			isHostsTurn = false;
			targetField = hostField;
			playerConnection = guestConnection;
			targetConnection = hostConnection;
		} else {
			throw new ServerException("Player doesn't belong to this game, or it's not the players turn");
		}
		if(xPos < 0 || xPos >= GAMEFIELD_SIZE
			|| yPos < 0 || yPos >= GAMEFIELD_SIZE) {
			throw new ServerException("Target-coordinates out of gamefield bounds!");
		}
		if(targetField[xPos][yPos] == CoordinateState.HIT
			|| targetField[xPos][yPos] == CoordinateState.MISS) {
			throw new ServerException("Field has already been shot!");

		} else if(targetField[xPos][yPos] == CoordinateState.EMPTY) {

			//Write non hit at coordiantes
			targetField[xPos][yPos] = CoordinateState.MISS;
			playerConnection.writePacket(new GameShootResponsePacket(false, false,
				false, xPos, yPos));
			//write non hit at coordiantes
			targetConnection.writePacket(new GameShootResponsePacket(true, false,
				false, xPos, yPos));
			//Let other player try a shot
			setState(isHostsTurn? new GuestsTurnState() : new HostsTurnState());
			targetConnection.writePacket(new GamePlayersTurnPacket());
			playerConnection.writePacket(new GameEnemiesTurnPacket());
		} else {

			//Hit a ship!
			targetField[xPos][yPos] = CoordinateState.HIT;
			boolean isHasGameEnded = checkForGameEnd();
			//Write hit at coordinates
			playerConnection.writePacket(new GameShootResponsePacket(false, true,
				isHasGameEnded, xPos, yPos));
			//write hit at coordiantes
			targetConnection.writePacket(new GameShootResponsePacket(true, true,
				isHasGameEnded, xPos, yPos));
			//Let player shoot again
			if(!isHasGameEnded) {
				targetConnection.writePacket(new GameEnemiesTurnPacket());
				playerConnection.writePacket(new GamePlayersTurnPacket());
			}
		}


	}

	private synchronized boolean checkForGameEnd() {
		return checkForAllHit(hostField) || checkForAllHit(guestField);
	}

	private synchronized boolean checkForAllHit(CoordinateState[][] gameField) {
		for(CoordinateState[] col : gameField) {
			for(CoordinateState state : col) {
				if(state == CoordinateState.SHIP) {
					return false;
				}
			}
		}
		return true;
	}

	public synchronized void addGuest(final Player guest) throws ServerException {
		if (this.guest == null && state.isWaitingForGuest()) {
			this.guest = guest;

			guestConnection = new GameConnection(loadConnection(guest), this);
			hostConnection = loadConnection(host);
			hostConnection.writePacket(new GamePlayerDoSetupPacket(guest.getUsername()));
			guestConnection.writePacket(new GameJoinedPacket(getId()));
			guestConnection.writePacket(new GamePlayerDoSetupPacket(host.getUsername()));
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
				guestConnection.writePacket(new ServerErrorPacket(ServerErrorType.CRITICAL, host.getUsername() + " hat das Spiel verlassen!"));
			} else {
				hostConnection.writePacket(new ServerErrorPacket(ServerErrorType.CRITICAL,guest.getUsername() + " hat das Spiel verlassen!"));
			}
			setState(new UninitializedState());
		}
		GAME_SERVICE.removeGame(this);
	}

	public void sendChatMessage(final String fromUser, final String message) {
		LOGGER.info("[{}] CHAT: {}: {}", id.toString(), fromUser, message);
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

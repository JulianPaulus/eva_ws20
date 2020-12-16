package battleships.server.game;

import battleships.model.CoordinateState;
import battleships.model.Ship;
import battleships.model.ShipType;
import battleships.net.connection.Connection;
import battleships.net.connection.ConnectionEvent;
import battleships.net.packet.SendPacket;
import battleships.observable.Observable;
import battleships.observable.Observer;
import battleships.server.connection.AuthenticatedConnection;
import battleships.server.connection.GameConnection;
import battleships.server.exception.IllegalShipPositionException;
import battleships.server.exception.ServerException;
import battleships.server.game.gameState.*;
import battleships.server.packet.send.*;
import battleships.server.service.ConnectionService;
import battleships.server.service.GameService;
import battleships.util.ServerErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
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
			//Check that ships stay in fieldBounds and cause no ArrayOutOfBounds
			int startX = ship.getxCoordinate();
			int startY = ship.getyCoordinate();
			int endX = startX;
			int endY = startY;
			if(ship.isHorizontal()) {
				endX += ship.getType().getSize() - 1;
			} else {
				endY += ship.getType().getSize() - 1;
			}
			if(startX < 0 || startX >= GAMEFIELD_SIZE
				|| endX < 0 || endX >= GAMEFIELD_SIZE
				|| startY < 0 || startY >= GAMEFIELD_SIZE
				|| endY < 0 || endY >= GAMEFIELD_SIZE) {
				throw new IllegalShipPositionException("Ship out of gamefield bounds!");
			}
			//Set ship to field and make sure that a distance of one cell is kept between the
			// ships (horizontal, vertical, diagonal)
			for(int i = startX; i <= endX; i++) {
				for(int j = startY; j <= endY; j++) {
					boolean isPositionLegal = true;
					//Horizontal (with outOfBounds guard)
					isPositionLegal &= (i - 1) < 0 || gameField[i - 1][j] == CoordinateState.EMPTY;
					isPositionLegal &= (i + 1 ) >= GAMEFIELD_SIZE || gameField[i + 1][j] == CoordinateState.EMPTY;
					isPositionLegal &= gameField[i][j] == CoordinateState.EMPTY;
					//Vertical
					isPositionLegal &= (j - 1) < 0 || gameField[i][j - 1] == CoordinateState.EMPTY;
					isPositionLegal &= (j + 1 ) >= GAMEFIELD_SIZE || gameField[i][j + 1] == CoordinateState.EMPTY;
					//Diagonal
					isPositionLegal &= (i - 1) < 0 || (j - 1) < 0 || gameField[i - 1][j - 1] == CoordinateState.EMPTY;
					isPositionLegal &= (i + 1) >= GAMEFIELD_SIZE || (j + 1) >= GAMEFIELD_SIZE || gameField[i + 1][j + 1] == CoordinateState.EMPTY;
					isPositionLegal &= (i - 1) < 0 || (j + 1) >= GAMEFIELD_SIZE || gameField[i - 1][j + 1] == CoordinateState.EMPTY;
					isPositionLegal &= (i + 1) >= GAMEFIELD_SIZE || (j - 1) < 0 || gameField[i + 1][j - 1] == CoordinateState.EMPTY;
					if(!isPositionLegal) {
						throw new IllegalShipPositionException("Position Illegal! Some ships are too close together or overlap!");
					}
				}
			}
			//Place Ship
			for(int i = startX; i <= endX; i++) {
				for(int j = startY; j <= endY; j++) {
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
		if(playerId == host.getId() && state.canHostSetShip()) {
			targetField = guestField;
			playerConnection = hostConnection;
			targetConnection = guestConnection;
		} else if(playerId == guest.getId() && state.canGuestSetShip()) {
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
			//TODO
			playerConnection.writePacket();
			//write non hit at coordiantes
			hostConnection.writePacket();
			//Let other player try a shot
			hostConnection.writePacket(new GamePlayersTurnPacket());
			playerConnection.writePacket(new GameEnemiesTurnPacket());
		} else {

			//Hit a ship!
			targetField[xPos][yPos] = CoordinateState.HIT;
			//Write hit at coordinates
			playerConnection.writePacket();
			//write hit at coordiantes
			hostConnection.writePacket();
			//Let player shoot again
			hostConnection.writePacket(new GameEnemiesTurnPacket());
			playerConnection.writePacket(new GamePlayersTurnPacket());
		}


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
				guestConnection.writePacket(new ServerErrorPacket(ServerErrorType.CRITICAL, host.getUsername() + " disconnected!"));
			} else {
				hostConnection.writePacket(new ServerErrorPacket(ServerErrorType.CRITICAL,guest.getUsername() + " disconnected!"));
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

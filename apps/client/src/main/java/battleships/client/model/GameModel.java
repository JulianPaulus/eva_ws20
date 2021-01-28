package battleships.client.model;

import battleships.client.ClientMain;
import battleships.client.game.StatusMessageType;
import battleships.client.packet.send.PlayerReadyPacket;
import battleships.model.CoordinateState;
import battleships.model.Ship;
import battleships.model.ShipType;
import battleships.net.exception.IllegalShipPositionException;
import battleships.util.Constants;
import battleships.util.Utils;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GameModel {

	ShipType currentShip;

	private GameState currentState;
	private CoordinateState[][] playerField;
	private CoordinateState[][] targetField;
	private List<TextFlow> chat;
	private Ship[] ships;
	private Ship lastAdded;

	private final ModelObserver observer;
	private String otherPlayerName;

	public GameModel(final ModelObserver observer) {
		currentState = GameState.PENDING;
		chat = new LinkedList<>();
		reset();

		this.observer = observer;
	}

	private synchronized void reset() {
		playerField = new CoordinateState[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
		targetField = new CoordinateState[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
		ships = new Ship[Constants.SHIP_COUNT];

		for (int i = 0; i < Constants.BOARD_SIZE; i++) {
			Arrays.fill(playerField[i], CoordinateState.EMPTY);
			Arrays.fill(targetField[i], CoordinateState.EMPTY);
		}

		currentShip = ShipType.TWO_TILES;
	}

	public synchronized void rematch() {
		reset();
		setCurrentState(GameState.SET_UP);
	}

	public synchronized int getTileNumberOfCurrentShip() {
		return currentShip.getSize();
	}

	public synchronized void setShip(int xPos, int yPos, boolean horizontal) {

		Ship ship = new Ship(currentShip, xPos, yPos, horizontal);

		if (!checkForShipAvailability(ship))
			return;
		//send to Server, if success do the following

		for (int i = 0; i < 5; i++) {
			if (ships[i] == null) {
				ships[i] = ship;
				break;
			}
		}

		if (horizontal) {
			if (xPos + currentShip.getSize() <= Constants.BOARD_SIZE) {
				for (int i = 0; i < currentShip.getSize(); i++) {
					playerField[xPos + i][yPos] = CoordinateState.SHIP;
				}
			}

		} else {
			if (yPos + currentShip.getSize() <= Constants.BOARD_SIZE) {
				for (int i = 0; i < currentShip.getSize(); i++) {
					playerField[xPos][yPos + i] = CoordinateState.SHIP;
				}
			}
		}

		lastAdded = ship;
		switchToNextBiggerShipType();

		observer.notifyAboutPlayerModelChange();
	}

	public synchronized boolean checkForShipAvailability(final Ship ship) {
		try {
			Utils.validateShip(ship, playerField);
		} catch (final IllegalShipPositionException e) {
			return false;
		}
		return true;
	}

	public synchronized GameState getCurrentState() {
		return currentState;
	}


	public synchronized void setCurrentState(GameState currentState) {//setzt Status von Server
		this.currentState = currentState;
		observer.notifyAboutGameStatusChange();
	}

	public synchronized CoordinateState getTargetFieldState(int xPos, int yPos) {
		return getFieldState(this.targetField, xPos, yPos);
	}

	public synchronized CoordinateState getPlayerFieldState(int xPos, int yPos) {
		return getFieldState(this.playerField, xPos, yPos);
	}

	private synchronized CoordinateState getFieldState(CoordinateState[][] field, int xPos, int yPos) {
		if(xPos < 0 || xPos >= field.length || yPos < 0 || yPos >= field.length) {
			return null;
		}
		return targetField[xPos][yPos];
	}

	public synchronized void setTargetFieldState(int xPos, int yPos, CoordinateState state) {
		this.targetField[xPos][yPos] = state;
		observer.notifyAboutTargetModelChange();
	}

	public synchronized void setPlayerFieldState(int xPos, int yPos, CoordinateState state) {
		this.playerField[xPos][yPos] = state;
		observer.notifyAboutPlayerModelChange();
	}

	public synchronized CoordinateState currentStateOfPlayerCoordinate(int xPos, int yPos) {
		return playerField[xPos][yPos];
	}

	public synchronized void receiveChatMessage(final String fromUser, final String message) {
		chat.add(createChatMessage(fromUser, message));
		observer.notifyAboutChatMessage();
	}

	public synchronized void receiveStatusMessage(final String message, final StatusMessageType type) {
		chat.add(createStatusMessage(message, type.getStyle()));
		observer.notifyAboutChatMessage();
	}

	private synchronized TextFlow createChatMessage(final String fromUser, final String message) {
		Text username = new Text(fromUser);
		username.getStyleClass().add("username");
		Text content = new Text(": " + message);
		TextFlow flow = new TextFlow();
		flow.getChildren().addAll(username, content);

		return flow;
	}

	private synchronized TextFlow createStatusMessage(final String message, final String style) {
		TextFlow flow = new TextFlow();
		Text text = new Text(message);
		text.setStyle(style);
		flow.getChildren().add(text);

		return flow;
	}

	public synchronized List<TextFlow> getChatMessages() {
		return chat;
	}

	synchronized void sendShipsToServer() {
		PlayerReadyPacket prp = new PlayerReadyPacket(this.ships);
		ClientMain.getInstance().getConnection().writePacket(prp);
	}

	public synchronized void switchToNextBiggerShipType()
	{
		long currentPlacedShipsOfType = Arrays.stream(ships).filter(x -> x != null && x.getType() == currentShip).count();
		if(currentShip.getNrPerField() <= currentPlacedShipsOfType) {
			if(currentShip.getNext() == null) {
				currentState= GameState.PENDING;
				this.sendShipsToServer();
				observer.notifyAboutGameStatusChange();
				return;
			}
			currentShip = currentShip.getNext();
		}
		observer.notifyAboutShipTypeChange();
	}

	public synchronized void switchToPreviousShipType()
	{
		long currentPlacedShipsOfType = Arrays.stream(ships).filter(x -> x != null && x.getType() == currentShip).count();
		if(currentPlacedShipsOfType == 0) {
			ShipType prevType = ShipType.getPrev(currentShip);
			if(prevType == null) {
				currentShip = ShipType.TWO_TILES;
			} else {
				currentShip = prevType;
			}
		}
		observer.notifyAboutShipTypeChange();
	}

	public synchronized void removeAllShips() {
		for (int i = 0; i < 10; i++) {
			Arrays.fill(playerField[i], CoordinateState.EMPTY);
		}
		this.ships = new Ship[5];
		this.lastAdded = null;
		this.currentShip = ShipType.getFirst();
		observer.notifyAboutPlayerModelChange();
	}

	public synchronized void removeLastAdded() {
		if (lastAdded == null) return;
		int xPos = lastAdded.getXCoordinate();
		int yPos = lastAdded.getYCoordinate();

		int shipLength = lastAdded.getType().getSize();

		for (int i = 0; i < shipLength; i++) {
			if (lastAdded.isHorizontal()) {
				playerField[xPos + i][yPos] = CoordinateState.EMPTY;
			} else {
				playerField[xPos][yPos + i] = CoordinateState.EMPTY;
			}

		}

		for (int i = 0; i < ships.length; i++) {
			if (ships[i]!=null&&ships[i].equals(lastAdded)) {
				ships[i] = null;

				if (i > 0) {
					lastAdded = ships[i - 1];
				}
				else
				{
					lastAdded=null;
				}
				switchToPreviousShipType();
				break;
			}
		}

		observer.notifyAboutPlayerModelChange();
	}

	public synchronized ShipType getCurrentShip() {
		return currentShip;
	}

	public synchronized void setOtherPlayerName(final String otherPlayerName) {
		this.otherPlayerName = otherPlayerName;
	}

	public synchronized String getOtherPlayerName() {
		return otherPlayerName;
	}

	public synchronized void setEnemyLeftOverShipPositions(final Ship[] enemyShips) {
		for (final Ship ship : enemyShips) {
			for (int i = 0; i < ship.getType().getSize(); i++) {
				int xOffset = ship.isHorizontal() ? i : 0;
				int yOffset = ship.isHorizontal() ? 0 : i;
				if (targetField[ship.getXCoordinate() + xOffset][ship.getYCoordinate() + yOffset] != CoordinateState.HIT) {
					targetField[ship.getXCoordinate() + xOffset][ship.getYCoordinate() + yOffset] = CoordinateState.SHIP;
				}
			}
		}
		observer.notifyAboutTargetModelChange();
	}
}

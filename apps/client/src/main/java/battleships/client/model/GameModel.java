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

	private void reset() {
		playerField = new CoordinateState[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
		targetField = new CoordinateState[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
		ships = new Ship[Constants.SHIP_COUNT];

		for (int i = 0; i < Constants.BOARD_SIZE; i++) {
			Arrays.fill(playerField[i], CoordinateState.EMPTY);
			Arrays.fill(targetField[i], CoordinateState.EMPTY);
		}

		currentShip = ShipType.TWO_TILES;
	}

	public void rematch() {
		reset();
		setCurrentState(GameState.SET_UP);
	}

	public int getTileNumberOfCurrentShip() {
		return currentShip.getSize();
	}

	public void setShip(int xPos, int yPos, boolean horizontal) {

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

	public boolean checkForShipAvailability(final Ship ship) {
		try {
			Utils.validateShip(ship, playerField);
		} catch (final IllegalShipPositionException e) {
			return false;
		}
		return true;
	}

	public GameState getCurrentState() {
		return currentState;
	}


	public void setCurrentState(GameState currentState) {//setzt Status von Server
		this.currentState = currentState;
		observer.notifyAboutGameStatusChange();
	}

	public CoordinateState getTargetFieldState(int xPos, int yPos) {
		return getFieldState(this.targetField, xPos, yPos);
	}

	public CoordinateState getPlayerFieldState(int xPos, int yPos) {
		return getFieldState(this.playerField, xPos, yPos);
	}

	private CoordinateState getFieldState(CoordinateState[][] field, int xPos, int yPos) {
		if(xPos < 0 || xPos >= field.length || yPos < 0 || yPos >= field.length) {
			return null;
		}
		return targetField[xPos][yPos];
	}

	public void setTargetFieldState(int xPos, int yPos, CoordinateState state) {
		this.targetField[xPos][yPos] = state;
		observer.notifyAboutTargetModelChange();
	}

	public void setPlayerFieldState(int xPos, int yPos, CoordinateState state) {
		this.playerField[xPos][yPos] = state;
		observer.notifyAboutPlayerModelChange();
	}

	public CoordinateState currentStateOfPlayerCoordinate(int xPos, int yPos) {
		return playerField[xPos][yPos];
	}

	public void receiveChatMessage(final String fromUser, final String message) {
		chat.add(createChatMessage(fromUser, message));
		observer.notifyAboutChatMessage();
	}

	public void receiveStatusMessage(final String message, final StatusMessageType type) {
		chat.add(createStatusMessage(message, type.getStyle()));
		observer.notifyAboutChatMessage();
	}

	private TextFlow createChatMessage(final String fromUser, final String message) {
		Text username = new Text(fromUser);
		username.getStyleClass().add("username");
		Text content = new Text(": " + message);
		TextFlow flow = new TextFlow();
		flow.getChildren().addAll(username, content);

		return flow;
	}

	private TextFlow createStatusMessage(final String message, final String style) {
		TextFlow flow = new TextFlow();
		Text text = new Text(message);
		text.setStyle(style);
		flow.getChildren().add(text);

		return flow;
	}

	public List<TextFlow> getChatMessages() {
		return chat;
	}

	void sendShipsToServer() {
		PlayerReadyPacket prp = new PlayerReadyPacket(this.ships);
		ClientMain.getInstance().getConnection().writePacket(prp);
	}

	public void switchToNextBiggerShipType()
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

	public void switchToPreviousShipType()
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

	public void removeAllShips() {
		for (int i = 0; i < 10; i++) {
			Arrays.fill(playerField[i], CoordinateState.EMPTY);
		}
		this.ships = new Ship[5];
		this.lastAdded = null;
		this.currentShip = ShipType.getFirst();
		observer.notifyAboutPlayerModelChange();
	}

	public void removeLastAdded() {
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
			if (ships[i].equals(lastAdded)) {
				ships[i] = null;

				if (i > 0) {
					lastAdded = ships[i - 1];
				}
				switchToPreviousShipType();
				break;
			}
		}

		observer.notifyAboutPlayerModelChange();
	}

	public void setPlayerLost() {
		currentState = GameState.LOST;
		observer.notifyAboutGameStatusChange();
	}

	public void setPlayerWon() {
		currentState = GameState.WON;
		observer.notifyAboutGameStatusChange();
	}

	public ShipType getCurrentShip() {
		return currentShip;
	}

	public void setOtherPlayerName(final String otherPlayerName) {
		this.otherPlayerName = otherPlayerName;
	}

	public String getOtherPlayerName() {
		return otherPlayerName;
	}
}

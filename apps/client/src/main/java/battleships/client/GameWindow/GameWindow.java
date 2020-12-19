package battleships.client.GameWindow;

import battleships.client.ClientMain;
import battleships.client.Model.GameModel;
import battleships.client.Model.GameState;
import battleships.client.Model.ModelObserver;
import battleships.client.packet.send.SendChatMessagePacket;
import battleships.client.packet.send.ShootPacket;
import battleships.model.CoordinateState;
import battleships.model.Ship;
import battleships.util.Constants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

public class GameWindow implements Initializable {

	private static final Logger LOGGER = LoggerFactory.getLogger(GameWindow.class);
	private static GameWindow INSTANCE;

	private Stage stage;

	@FXML
	private HBox root;

	@FXML
	private GridPane playerGrid;

	@FXML
	private GridPane targetGrid;

	@FXML
	private Label statusLabel; //Wird dem Nutzer sagen, inn welcher Phase sich das Spiel befindet: Schiffe setzen, Zielen, warten auf Gegner,Gewonnen/verloren

	@FXML
	private TextField chatTextBox;

	@FXML
	private ListView<TextFlow> chatWindow;

	@FXML
	private WebView rulesView;

	@FXML
	private Button removeShip;

	@FXML
	private Button sendMessageBtn;

	private GameModel model;
	private boolean horizontal;

	private Label[][] playerLabels = new Label[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
	private Label[][] targetLabels = new Label[Constants.BOARD_SIZE][Constants.BOARD_SIZE];

	public GameWindow() {
		model = new GameModel(new ModelObserver(this));
		horizontal = true;

		INSTANCE = this;
	}

	public static void openWindow(final Stage stage, final CountDownLatch latch) {
		Platform.runLater(() -> {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.load(GameWindow.class.getResourceAsStream("/fxml/GameWindow.fxml"));
				Scene scene = new Scene(loader.getRoot());
				scene.getStylesheets().add(GameWindow.class.getResource("/fxml/style.css").toString());
				INSTANCE = loader.getController();
				latch.countDown();
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@FXML
	public void sendMessage() {
		String text = chatTextBox.getText().trim();
		if (!text.isEmpty()) {
			ClientMain.getInstance().getConnection().writePacket(new SendChatMessagePacket(text));
		}
		chatTextBox.clear();
	}

	public void receiveMessage(final String fromUser, final String message) {
		model.receiveChatMessage(fromUser, message);
	}

	public void displayStatusMessage(final String message, final StatusMessageType type) {
		model.receiveStatusMessage(message, type);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rulesView.getEngine().setUserStyleSheetLocation(GameWindow.class.getResource("/fxml/webView.css").toString());
		updateRulesForPhaseChange();

		setupBoard(playerGrid, playerLabels);
		setupBoard(targetGrid, targetLabels);
	}

	private void setupBoard(GridPane gridPane, Label[][] labelArray) {
		for (int i = 0; i < Constants.BOARD_SIZE; i++) {
			final int finalI = i;
			for (int j = 0; j < Constants.BOARD_SIZE; j++) {
				final int finalJ = j;
				final Label label = createLabelForCoordinate(i, j);
				gridPane.add(label, i, j);
				labelArray[i][j] = label;
				if (gridPane == targetGrid) {
					label.setOnMouseEntered(event -> {
						onMouseEnterTargetField(label);
					});
					label.setOnMouseExited(event -> {
						onMouseExitTargetField(label, finalI, finalJ);
					});
					label.setOnMouseClicked(event -> onTargetFieldClicked(finalI, finalJ));
				} else {
					label.setOnMouseEntered(event -> {
						onMouseHoverPlayerField(finalI, finalJ, true);
					});
					label.setOnMouseExited(event -> {
						onMouseHoverPlayerField(finalI, finalJ, false);
					});
					label.setOnMouseClicked(event -> {
						if (event.getButton() == MouseButton.PRIMARY)
							onPlayerFieldClicked(finalI, finalJ);
						else if (event.getButton() == MouseButton.SECONDARY)
							onPlayerFieldRightClicked(finalI, finalJ);
					});
				}
			}
		}
	}

	private void onMouseEnterTargetField(final Label label) {
		if (model.getCurrentState() != GameState.SHOOTING) return;

		label.setStyle(CoordinateState.TARGETING.getStyle());
	}

	private void onMouseExitTargetField(final Label label, final int posX, final int posY) {
		if (model.getCurrentState() != GameState.SHOOTING) return;

		CoordinateState targetState = model.getTargetFieldState(posX, posY);
		label.setStyle(targetState.getStyle());
	}

	private void onMouseHoverPlayerField(final int posX, final int posY, final boolean entered) {
		if (model.getCurrentState() != GameState.SET_UP) return;
		if (horizontal && posX + model.getTileNumberOfCurrentShip() > Constants.BOARD_SIZE) return;
		if (!horizontal && posY + model.getTileNumberOfCurrentShip() > Constants.BOARD_SIZE) return;

		Ship tempShip = new Ship(model.getCurrentShip(), posX, posY, horizontal);
		boolean isValid = model.checkForShipAvailability(tempShip);
		for (int offset = 0; offset < model.getTileNumberOfCurrentShip(); offset++) {
			int x = posX + (horizontal ? offset : 0);
			int y = posY + (horizontal ? 0 : offset);
			if (entered) {
				updateShipFieldOnEnter(x, y, isValid);
			} else {
				resetPlayerFieldLabel(x, y);
			}
		}
	}

	private void updateShipFieldOnEnter(final int posX, final int posY, final boolean isValid) {
		if (isValid) {
			playerLabels[posX][posY].setStyle(CoordinateState.SHIP.getStyle());
		} else {
			playerLabels[posX][posY].setStyle(CoordinateState.INVALID.getStyle());
		}
	}

	private void resetPlayerFieldLabel(final int posX, final int posY) {
		CoordinateState state = model.currentStateOfPlayerCoordinate(posX, posY);
		playerLabels[posX][posY].setStyle(state.getStyle());
	}

	void onPlayerFieldRightClicked(final int posX, final int posY) {
		if (model.getCurrentState() != GameState.SET_UP) return;

		for (int offset = 0; offset < model.getTileNumberOfCurrentShip(); offset++) {
			int x = posX + (horizontal ? offset : 0);
			int y = posY + (horizontal ? 0 : offset);
			if (x < Constants.BOARD_SIZE && y < Constants.BOARD_SIZE) {
				resetPlayerFieldLabel(x, y);
			}
		}
		horizontal = !horizontal;
		onMouseHoverPlayerField(posX, posY, true);
	}

	void onPlayerFieldClicked(int xPos, int yPos) {
		if (model.getCurrentState() != GameState.SET_UP) return;
		model.setShip(xPos, yPos, horizontal);
	}

	void onTargetFieldClicked(int xPos, int yPos) {
		if (model.getCurrentState() != GameState.SHOOTING
			|| model.getTargetFieldState(xPos, yPos) != CoordinateState.EMPTY) {
			return;
		}
		model.setCurrentState(GameState.SHOOTING_WAIT_FOR_RESPONSE);
		ClientMain.getInstance().getConnection().writePacket(new ShootPacket(xPos, yPos));
		//Todo Anzeigen, dass auf den Server gewartet wird?
	}

	public void updateChatWindow() {
		Platform.runLater(() -> {
			chatWindow.getItems().clear();
			chatWindow.getItems().addAll(model.getChatMessages());
			chatWindow.scrollTo(model.getChatMessages().size());
		});
	}

	public void updatePlayerField() {
		for (int x = 0; x < Constants.BOARD_SIZE; x++)
			for (int y = 0; y < Constants.BOARD_SIZE; y++) {
				CoordinateState state = model.currentStateOfPlayerCoordinate(x, y);
				playerLabels[x][y].setStyle(state.getStyle());
			}
	}

	public void updateTargetField() {
		for (int x = 0; x < Constants.BOARD_SIZE; x++)
			for (int y = 0; y < Constants.BOARD_SIZE; y++) {
				CoordinateState state = model.getTargetFieldState(x, y);
				targetLabels[x][y].setStyle(state.getStyle());
			}
	}

	public void updateRulesForPhaseChange() {
		model.getCurrentState().updateGameWindow(this);
	}

	@FXML
	public void removeLastAddedShip() {
		model.removeLastAdded();
	}

	public static GameWindow getInstance() {
		return INSTANCE;
	}

	public void onDoSetup(final String otherPlayerName) {
		model.setOtherPlayerName(otherPlayerName);
		model.setCurrentState(GameState.SET_UP);
		sendMessageBtn.setDisable(false);
		chatTextBox.setDisable(false);
		updateRulesForPhaseChange();
	}

	public void setHitOrMiss(boolean isPlayerField, boolean isHit, int xPos, int yPos) {
		if(isPlayerField) {
			model.setPlayerFieldState(xPos, yPos, isHit? CoordinateState.HIT : CoordinateState.MISS);
			displayStatusMessage("Der Gegner hat auf " + intToAlphabet(xPos) + (yPos + 1) + " geschossen.", StatusMessageType.INFO);
		} else {
			model.setTargetFieldState(xPos, yPos, isHit? CoordinateState.HIT : CoordinateState.MISS);
			displayStatusMessage("Sie haben auf " + intToAlphabet(xPos) + (yPos + 1) + " geschossen.", StatusMessageType.INFO);
		}
	}

	public void onWaitForOtherPlayerSetup() {
		model.setCurrentState(GameState.SET_UP_WAIT_FOR_OTHER_PLAYER);
		updateRulesForPhaseChange();
	}


	public void removeAllShips() {
		model.removeAllShips();
		model.setCurrentState(GameState.SET_UP);
		updateRulesForPhaseChange();
	}

	public void onPlayersTurn() {
		model.setCurrentState(GameState.SHOOTING);
		updateRulesForPhaseChange();
	}

	public void onEnemyTurn() {
		model.setCurrentState(GameState.WAIT_FOR_ENEMY);
		updateRulesForPhaseChange();
	}

	public void setGameEnd(boolean isHasPlayerWon) {
		model.setCurrentState(isHasPlayerWon? GameState.WON : GameState.LOST);
	}

	public void setRemoveShipButtonVisible(final boolean visible) {
		removeShip.setVisible(visible);
	}

	public void setStatusLabelStyle(final String style) {
		statusLabel.setStyle(style);
	}

	public void updateStatusText() {
		statusLabel.setText(model.getCurrentState().getStatusText());
	}

	public void updateRulesText() {
		rulesView.getEngine().loadContent(model.getCurrentState().getRuleText());
	}

	public void disableChat() {
		chatTextBox.setDisable(true);
		sendMessageBtn.setDisable(true);
	}

	private static char intToAlphabet(int i) {
		return (char) (65 + i);
	}

	private static Label createLabelForCoordinate(int x, int y) {
		final Label label = new Label();
		label.setTextAlignment(TextAlignment.CENTER);
		label.setMaxHeight(Double.MAX_VALUE);
		label.setMaxWidth(Double.MAX_VALUE);
		label.setStyle(CoordinateState.EMPTY.getStyle());
		if (y == 0) {
			label.setText(String.valueOf(intToAlphabet(x)));
		}
		if (x == 0) {
			label.setText(label.getText() + (y + 1));
		}
		label.setAlignment(Pos.TOP_LEFT);
		label.getStyleClass().add("pos-label");

		return label;
	}

	public void onPlayerDisconnected() {
		displayStatusMessage(model.getOtherPlayerName() + " hat das Spiel verlassen.", StatusMessageType.CRITICAL);
		model.setCurrentState(GameState.OTHER_PLAYER_DISCONNECTED);
	}
}

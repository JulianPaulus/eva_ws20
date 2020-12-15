package battleships.client.GameWindow;

import battleships.client.ClientMain;
import battleships.client.Model.GameModel;
import battleships.client.Model.GameState;
import battleships.client.Model.ModelObserver;
import battleships.client.packet.send.SendChatMessagePacket;
import battleships.model.CoordinateState;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class GameWindow implements Initializable {

	private static GameWindow INSTANCE;

	private final int BOARD_SQUARE_SIZE = 10;

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
	private ListView chatWindow;

	@FXML
	private TextArea rulesTextArea;

	@FXML
	private Button removeShip;

	private GameModel model;
	private boolean horizontal;

	private Label[][] playerLabels = new Label[BOARD_SQUARE_SIZE][BOARD_SQUARE_SIZE];
	private Label[][] targetLabels = new Label[BOARD_SQUARE_SIZE][BOARD_SQUARE_SIZE];

	public GameWindow() {
		model = new GameModel(new ModelObserver(this));
		horizontal = true;

		INSTANCE = this;
	}

	@FXML
	public void sendMessage() {
		ClientMain.getInstance().getConnection().writePacket(new SendChatMessagePacket(chatTextBox.getText().trim()));
		chatTextBox.clear();
	}

	public void receiveMessage(final String fromUser, final String message) {
		model.receiveChatMessage(fromUser, message);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		updateRulesForPhaseChange();

		setupBoard(playerGrid, playerLabels);
		setupBoard(targetGrid, targetLabels);
	}

	private void setupBoard(GridPane gridPane, Label[][] labelArray) {
		for (int i = 0; i < BOARD_SQUARE_SIZE; i++) {
			final int finalI = i;
			for (int j = 0; j < BOARD_SQUARE_SIZE; j++) {
				final int finalJ = j;
				final Label label = new Label();
				label.setTextAlignment(TextAlignment.CENTER);
				GridPane.setHalignment(label, HPos.CENTER);
				GridPane.setValignment(label, VPos.CENTER);
				label.setMaxHeight(Double.MAX_VALUE);
				label.setMaxWidth(Double.MAX_VALUE);
				label.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: black");
				GridPane.setHgrow(label, Priority.ALWAYS);
				GridPane.setVgrow(label, Priority.ALWAYS);
				gridPane.add(label, i, j);
				labelArray[i][j] = label;
				if (gridPane == targetGrid) {
					label.setOnMouseEntered(event -> {
//						System.out.println("Mouse entered Field: " + finalI + ", " + finalJ);
						onMouseHoverTargetField(label, finalI, finalJ, true);
					});
					label.setOnMouseExited(event -> {
//						System.out.println("Mouse exited Field: " + finalI + ", " + finalJ);
						onMouseHoverTargetField(label, finalI, finalJ, false);
					});
					label.setOnMouseClicked(event -> onTargetFieldClicked(finalI, finalJ));
				} else {
					label.setOnMouseEntered(event -> {
//						System.out.println("Mouse entered Field: " + finalI + ", " + finalJ);
						onMouseHoverPlayerField(finalI, finalJ, true);
					});
					label.setOnMouseExited(event -> {
//						System.out.println("Mouse exited Field: " + finalI + ", " + finalJ);
						onMouseHoverPlayerField(finalI, finalJ, false);
					});
					label.setOnMouseClicked(event -> {
						if (event.getButton() == MouseButton.PRIMARY)
							onPlayerFieldClicked(finalI, finalJ);
						else if (event.getButton() == MouseButton.SECONDARY)
							onPlayerFieldRightClicked(label, finalI, finalJ);
					});
				}
			}
		}
	}

	private void onMouseHoverTargetField(Label label, int posX, int poxY, boolean isEnter) {
		if (isEnter) {
			if (model.getCurrentState() == GameState.SHOOTING) {
				label.setStyle("-fx-background-color: #e6f54f;" + "-fx-border-color: black");
			}
		} else {
			if (model.getCurrentState() == GameState.SHOOTING) {

				if (model.currentStateOfTargetCoordinate(posX, poxY) == CoordinateState.EMPTY) {
					label.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: black");
				} else if (model.currentStateOfTargetCoordinate(posX, poxY) == CoordinateState.HIT) {
					label.setStyle("-fx-background-color: #ea1313;" + "-fx-border-color: black");
				} else if (model.currentStateOfTargetCoordinate(posX, poxY) == CoordinateState.MISS) {
					label.setStyle("-fx-background-color: #bdbdbd;" + "-fx-border-color: black");
				}
			}
		}
	}

	private void onMouseHoverPlayerField(int posX, int posY, boolean isEnter) {
		if (model.getCurrentState() == GameState.SET_UP) {
//			System.out.println("onMouseHoverPlayerField" + isEnter + " " + posX + " " + posY);
			if (isEnter) {

//				System.out.println("onMouseHoverPlayerField" + posX + " " + posY);


				if (horizontal) {
					if (posX + model.getTileNumberOfCurrentShip() <= 10) {
						for (int x = 0; x < model.getTileNumberOfCurrentShip(); x++) {
//							System.out.println("" + posX + posY);
							playerLabels[posX + x][posY]
								.setStyle("-fx-background-color: #0004ff;" + "-fx-border-color: black");
						}
					}
				} else {
					if (posY + model.getTileNumberOfCurrentShip() <= 10) {
						for (int y = 0; y < model.getTileNumberOfCurrentShip(); y++) {
//							System.out.println("" + posX + posY);
							playerLabels[posX][posY + y]
								.setStyle("-fx-background-color: #0004ff;" + "-fx-border-color:black");
						}
					}
				}
			} else {
				if (horizontal) {
					if (posX + model.getTileNumberOfCurrentShip() <= 10) {
						for (int x = 0; x < model.getTileNumberOfCurrentShip(); x++) {
//							System.out.println("" + posX + posY);
							if (model.currentStateOfPlayerCoordinate(posX + x, posY) == CoordinateState.EMPTY)
								playerLabels[posX + x][posY]
									.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: black");
							else
								playerLabels[posX + x][posY]
									.setStyle("-fx-background-color: #0004ff;" + "-fx-border-color: black");
						}
					}
				} else {
					if (posY + model.getTileNumberOfCurrentShip() <= 10) {
						for (int y = 0; y < model.getTileNumberOfCurrentShip(); y++) {
//							System.out.println("" + posX + " " + (posY + y));
							if (model.currentStateOfPlayerCoordinate(posX, posY + y) == CoordinateState.EMPTY) {
								playerLabels[posX][posY + y]
									.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: black");
							} else {
//								System.out.println("Ship");
								playerLabels[posX][posY + y]
									.setStyle("-fx-background-color: #0004ff;" + "-fx-border-color: black");
							}
						}
					}
				}
			}
		}
	}

	void onPlayerFieldRightClicked(Label label, int xPos, int yPos) {
		if (model.getCurrentState() == GameState.SET_UP) {
			if (horizontal) {

				if (xPos + model.getTileNumberOfCurrentShip() <= 10) {
					for (int x = 0; x < model.getTileNumberOfCurrentShip(); x++) {
						switch (model.currentStateOfPlayerCoordinate(xPos + x, yPos)) {
							case SHIP:
								playerLabels[xPos + x][yPos]
									.setStyle("-fx-background-color: #0004ff;" + "-fx-border-color: black");
								if (xPos + model.getTileNumberOfCurrentShip() <= 10)
									playerLabels[xPos][yPos + x]
										.setStyle("-fx-background-color: #0004ff;" + "-fx-border-color: black");
								break;
							case EMPTY:
								playerLabels[xPos + x][yPos]
									.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: black");
								if (xPos + model.getTileNumberOfCurrentShip() <= 10)
									playerLabels[xPos][yPos + x]
										.setStyle("-fx-background-color: #0004ff;" + "-fx-border-color: black");
								break;
						}
					}
				}
			} else {
				if (yPos + model.getTileNumberOfCurrentShip() < 10) {
					for (int y = 0; y < model.getTileNumberOfCurrentShip(); y++) {
						switch (model.currentStateOfPlayerCoordinate(xPos, yPos + y)) {
							case SHIP:
								playerLabels[xPos][yPos + y]
									.setStyle("-fx-background-color: #0004ff;" + "-fx-border-color: black");
								if (xPos + model.getTileNumberOfCurrentShip() <= 10)
									playerLabels[xPos + y][yPos]
										.setStyle("-fx-background-color: #0004ff;" + "-fx-border-color: black");
								break;
							case EMPTY:
								playerLabels[xPos][yPos + y]
									.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: black");
								if (xPos + model.getTileNumberOfCurrentShip() <= 10)
									playerLabels[xPos + y][yPos]
										.setStyle("-fx-background-color: #0004ff;" + "-fx-border-color: black");
								break;
						}
					}
				}
			}
			horizontal = (!horizontal);
		}
	}

	void onPlayerFieldClicked(int xPos, int yPos) {
		if (model.getCurrentState() == GameState.SET_UP)
			model.setShip(xPos, yPos, horizontal);
	}

	void onTargetFieldClicked(int xPos, int yPos) {
		if (model.getCurrentState() == GameState.SHOOTING)
			model.shootAt(xPos, yPos);
	}

	public void updateChatWindow() {
		chatWindow.getItems().clear();
		for (String message : model.getChatMessages())
			try {
				chatWindow.getItems().add(message);
			} catch (Exception e) {
				Alert alert = new Alert(Alert.AlertType.ERROR,
					"Could not add message:\n" + e.getMessage() + e.getCause());
				e.printStackTrace();
			}

	}

	public void updatePlayerField() {
		for (int x = 0; x < BOARD_SQUARE_SIZE; x++)
			for (int y = 0; y < BOARD_SQUARE_SIZE; y++) {
				switch (model.currentStateOfPlayerCoordinate(x, y)) {
					case SHIP:
						playerLabels[x][y].setStyle("-fx-background-color: #0004ff;" + "-fx-border-color: #000000");
						break;
					case HIT:
						playerLabels[x][y].setStyle("-fx-background-color: #ea1313;" + "-fx-border-color: black");
						break;

					case MISS:
						playerLabels[x][y].setStyle("-fx-background-color: #bdbdbd;" + "-fx-border-color: black");
						break;

					default:
						playerLabels[x][y].setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: black");
				}
			}
	}

	public void updateTargetField() {
		for (int x = 0; x < BOARD_SQUARE_SIZE; x++)
			for (int y = 0; y < BOARD_SQUARE_SIZE; y++) {
				switch (model.currentStateOfTargetCoordinate(x, y)) {
					case HIT:
						targetLabels[x][y].setStyle("-fx-background-color: #ea1313;" + "-fx-border-color: black");
						break;

					case MISS:
						targetLabels[x][y].setStyle("-fx-background-color: #bdbdbd;" + "-fx-border-color: black");
						break;

					default:
						targetLabels[x][y].setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: black");
				}
			}
	}

	public void updateRulesForPhaseChange() {
		if (model.getCurrentState() == GameState.SET_UP) {
			Platform.runLater(() -> {
				statusLabel.setText("Bitte Schiffe setzen");
				rulesTextArea.clear();
				rulesTextArea.setText("setzen sie ihre Schiffe:\n" +
					"Beim Hovern \u00FCber dem Spielfeld wird die derzeitige Position des Schiffs angezeigt.\n" +
					"Mit Rechtsklick \u00E4ndern sie die Ausrichtung (Horizontal/Vertikal)\n" +
					"Mit linksklick setzen sie das Schiff\n" +
					"Schiffe werden Blau dargestellt");
				removeShip.setVisible(true);
			});

		} else if (model.getCurrentState() == GameState.SHOOTING) {
			Platform.runLater(() -> {
				statusLabel.setText("Bitte Zielen");
				rulesTextArea.clear();
				rulesTextArea.setText(
					"Klicken sie auf das Zielen spielfeld, um auf die gew\u00FCnschte Position zu schie\u00DFen.\n" +
						"Treffer werden rot dargestellt, Verfehlungen werden grau dargestellt");
				removeShip.setVisible(false);
			});

		} else if (model.getCurrentState() == GameState.WAIT_FOR_ENEMY) {
			Platform.runLater(() -> {
				statusLabel.setText("Warten auf Gegner");
				rulesTextArea.clear();
				rulesTextArea.setText("Der Gegner schie\u00DFt, bitte warten.\n" +
					"Treffer auf ihren Schiffen werden rot dargestellt, Verfehlungen werden grau dargestellt");
				removeShip.setVisible(false);
			});

		} else if (model.getCurrentState() == GameState.WON) {
			Platform.runLater(() -> {
				statusLabel.setText("Gewonnen");
				statusLabel.setStyle("-fx-text-fill: green");
				rulesTextArea.clear();
			});

		} else if (model.getCurrentState() == GameState.LOST) {
			Platform.runLater(() -> {
				statusLabel.setText("Verloren");
				statusLabel.setStyle("-fx-text-fill: red");
				rulesTextArea.clear();
			});

		}
	}

	@FXML
	public void removeLastAddedShip() {
		model.removeLastAdded();
	}

	public static GameWindow getInstance() {
		return INSTANCE;
	}

	public void onDoSetup() {
		model.setCurrentState(GameState.SET_UP);
		updateRulesForPhaseChange();
	}


}

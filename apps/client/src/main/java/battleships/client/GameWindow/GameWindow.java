package battleships.client.GameWindow;

import battleships.client.Model.CoorrdinateStateEnum;
import battleships.client.Model.GameModel;
import battleships.client.Model.GameStateEnum;
import battleships.client.Model.ModelObserver;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

public class GameWindow implements Initializable {

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

	private Scene scene;
	private GameModel model;
	private boolean horizontal;

	private Label[][] playerLabels = new Label[BOARD_SQUARE_SIZE][BOARD_SQUARE_SIZE];
	private Label[][] targetLabels = new Label[BOARD_SQUARE_SIZE][BOARD_SQUARE_SIZE];

	public GameWindow(Stage stage)
	{
		horizontal=true;
		this.stage=stage;
		FXMLLoader fxmLLoader = new FXMLLoader(getClass().getResource("/fxml/GameWindow.fxml"));
		fxmLLoader.setController(this);
		try {
			HBox Layout=fxmLLoader.load();
			scene=new Scene(Layout);
		} catch (IOException e) {
			e.printStackTrace();
		}

		stage.setScene(scene);

		model= new GameModel(new ModelObserver(this));


	}

	private Node getNodeFromGridPane(GridPane gridPane, int xPos, int yPos) {

		System.out.println("xPos"+xPos+" yPos"+yPos+"gridPaneValid"+(gridPane!=null));
		for (Node node : gridPane.getChildren()) {
			if (GridPane.getColumnIndex(node) == xPos && GridPane.getRowIndex(node) == yPos) {
				return node;
			}
		}
		return null;
	}

	@FXML
	public void sendMessage()
	{
		model.sendChatMessage(chatTextBox.getText());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupBoard(playerGrid, playerLabels, BOARD_SQUARE_SIZE, BOARD_SQUARE_SIZE);
		setupBoard(targetGrid, targetLabels, BOARD_SQUARE_SIZE, BOARD_SQUARE_SIZE);
	}

	private void setupBoard(GridPane gridPane, Label[][] labelArray, int sizeX, int sizeY) {
		for (int i = 0; i < sizeX; i++) {
			final int finalI = i;
			for(int j = 0; j < sizeY; j++) {
				final int finalJ = j;
				final Label label = new Label();
				label.setTextAlignment(TextAlignment.CENTER);
				GridPane.setHalignment(label, HPos.CENTER);
				GridPane.setValignment(label, VPos.CENTER);
				label.setMaxHeight(Double.MAX_VALUE);
				label.setMaxWidth(Double.MAX_VALUE);
				label.setStyle("-fx-background-color: #ffffff;"+"-fx-border-color: black");
				GridPane.setHgrow(label, Priority.ALWAYS);
				GridPane.setVgrow(label, Priority.ALWAYS);
				gridPane.add(label, i, j);
				labelArray[i][j] = label;
				if(gridPane==targetGrid) {
					label.setOnMouseEntered(event -> {
						System.out.println("Mouse entered Field: " + finalI + ", " + finalJ);
						onMouseHoverTargetField(label, finalI, finalJ,  true);
					});
					label.setOnMouseExited(event -> {
						System.out.println("Mouse exited Field: " + finalI + ", " + finalJ);
						onMouseHoverTargetField(label, finalI, finalJ,  false);
					});
					label.setOnMouseClicked(event->{
						onTargetFieldClicked(finalI,finalJ);
					});
				}
				else
				{
					label.setOnMouseEntered(event -> {
						System.out.println("Mouse entered Field: " + finalI + ", " + finalJ);
						onMouseHoverPlayerField(label, finalI, finalJ,  true);
					});
					label.setOnMouseExited(event -> {
						System.out.println("Mouse exited Field: " + finalI + ", " + finalJ);
						onMouseHoverPlayerField(label, finalI, finalJ,  false);
					});
					label.setOnContextMenuRequested(event->{
						System.out.println("Mouse rightClickedOn Field: " + finalI + ", " + finalJ);
						onPlayerFieldRightClicked(label, finalI, finalJ);
					});
					label.setOnMouseClicked(event->{onPlayerFieldClicked( finalI, finalJ);});
				}
			}
		}
	}
	private void onMouseHoverTargetField(Label label, int posX, int poxY, boolean isEnter) {
		if(isEnter) {
			if(model.getCurrentState()==GameStateEnum.shooting) {
				label.setStyle("-fx-background-color: #e6f54f;"+"-fx-border-color: black");
			}
		} else {
			if(model.getCurrentState()==GameStateEnum.shooting) {

				if (model.currentStateOfTargetCoordinate(posX, poxY) == CoorrdinateStateEnum.Empty) {
					label.setStyle("-fx-background-color: #ffffff;"+"-fx-border-color: black");
					targetLabels[posX][poxY - 1].setStyle("-fx-background-color: #ffffff;"+"-fx-border-color: black");
				}
				else if (model.currentStateOfTargetCoordinate(posX, poxY) == CoorrdinateStateEnum.hit) {
					label.setStyle("-fx-background-color: #ea1313;"+"-fx-border-color: black");
					targetLabels[posX][poxY - 1].setStyle("-fx-background-color: #ea1313;"+"-fx-border-color: black");
				}
				else if (model.currentStateOfTargetCoordinate(posX, poxY) == CoorrdinateStateEnum.miss) {
					label.setStyle("-fx-background-color: #bdbdbd;"+"-fx-border-color: black");
					targetLabels[posX][poxY - 1].setStyle("-fx-background-color: #bdbdbd;"+"-fx-border-color: black");
				}
			}
		}
	}

	private void onMouseHoverPlayerField(Label label, int posX, int poxY, boolean isEnter) {
		if (model.getCurrentState() == GameStateEnum.setUp) {
			System.out.println("onMouseHoverPlayerField" + isEnter + " " + posX + " " + poxY);
			if (isEnter) {

				System.out.println("onMouseHoverPlayerField" + posX + " " + poxY);


				if (horizontal) {
					if (posX + model.getTileNumberOfCurrentShip() <= 10) {
						for (int x = 0; x < model.getTileNumberOfCurrentShip(); x++) {
							System.out.println("" + posX + poxY);
							playerLabels[posX + x][poxY].setStyle("-fx-background-color: #0004ff;"+"-fx-border-color: black");
						}
					}
				} else {
					if (poxY + model.getTileNumberOfCurrentShip() <= 10) {
						for (int y = 0; y < model.getTileNumberOfCurrentShip(); y++) {
							System.out.println("" + posX + poxY);
							playerLabels[posX][poxY + y].setStyle("-fx-background-color: #0004ff;"+"-fx-border-color:black");
						}
					}
				}
			}
		 else {
			if (horizontal) {
				if (posX + model.getTileNumberOfCurrentShip() <= 10) {
					for (int x = 0; x < model.getTileNumberOfCurrentShip(); x++) {
						System.out.println("" + posX + poxY);
						if(model.currentStateOfPlayerCoordinate(posX+x,poxY)==CoorrdinateStateEnum.Empty)
							playerLabels[posX + x][poxY].setStyle("-fx-background-color: #ffffff;"+"-fx-border-color: black");
						else
							playerLabels[posX + x][poxY].setStyle("-fx-background-color: #0004ff;"+"-fx-border-color: black");
					}
				}
			} else {
				if (poxY + model.getTileNumberOfCurrentShip() < 10) {
					for (int y = 0; y < model.getTileNumberOfCurrentShip(); y++) {
						System.out.println("" + posX + poxY);
						if (model.currentStateOfTargetCoordinate(posX, poxY) == CoorrdinateStateEnum.Empty){
							playerLabels[posX][poxY + y].setStyle("-fx-background-color: #ffffff;"+"-fx-border-color: black");
						}
						else {
							playerLabels[posX][poxY + y].setStyle("-fx-background-color: #0004ff;"+"-fx-border-color: black");
						}
					}
				}
			}
		}
		}
	}

	void onPlayerFieldRightClicked(Label label, int xPos, int yPos)
	{
		if(model.getCurrentState()==GameStateEnum.setUp)
		{
			if (horizontal)
			{

				if(xPos+model.getTileNumberOfCurrentShip()<=10)
				{
					for(int x=0;x<model.getTileNumberOfCurrentShip();x++)
					{
						switch(model.currentStateOfPlayerCoordinate(xPos+x,yPos)){
							case Ship:
								playerLabels[xPos+x][yPos].setStyle("-fx-background-color: #0004ff;"+"-fx-border-color: black");
								if(xPos+model.getTileNumberOfCurrentShip()<10)
									playerLabels[xPos][yPos+x].setStyle("-fx-background-color: #0004ff;"+"-fx-border-color: black");
								break;
							case Empty:
								playerLabels[xPos+x][yPos].setStyle("-fx-background-color: #ffffff;"+"-fx-border-color: black");
								if(xPos+model.getTileNumberOfCurrentShip()<10)
									playerLabels[xPos][yPos+x].setStyle("-fx-background-color: #0004ff;"+"-fx-border-color: black");
								break;
						}
					}
				}
			}
			else
			{
				if(yPos+model.getTileNumberOfCurrentShip()<10)
				{
					for(int y=0;y<model.getTileNumberOfCurrentShip();y++)
					{
						switch(model.currentStateOfPlayerCoordinate(xPos,yPos+y)){
							case Ship:
								playerLabels[xPos][yPos+y].setStyle("-fx-background-color: #0004ff;"+"-fx-border-color: black");
								if(xPos+model.getTileNumberOfCurrentShip()<10)
									playerLabels[xPos+y][yPos].setStyle("-fx-background-color: #0004ff;"+"-fx-border-color: black");
								break;
							case Empty:
								playerLabels[xPos][yPos+y].setStyle("-fx-background-color: #ffffff;"+"-fx-border-color: black");
								if(xPos+model.getTileNumberOfCurrentShip()<10)
									playerLabels[xPos+y][yPos].setStyle("-fx-background-color: #0004ff;"+"-fx-border-color: black");
								break;
						}
					}
				}
			}
			horizontal=(!horizontal);
		}
	}

	void onPlayerFieldClicked(int xPos, int yPos)
	{
		if(model.getCurrentState()==GameStateEnum.setUp)
			model.setShip(xPos,yPos,horizontal);
	}

	void onTargetFieldClicked(int xPos, int yPos)
	{
		if(model.getCurrentState()==GameStateEnum.shooting)
			model.shootAt(xPos,yPos);
	}

	public void updateChatWindow()
	{
		chatWindow.getItems().clear();
		for(String message:model.getChatMessages())
			chatWindow.getItems().add(message);
	}
	public void updatePlayerField()
	{
		for(int x=0;x<BOARD_SQUARE_SIZE;x++)
			for(int y=0;y<BOARD_SQUARE_SIZE;y++)
			{
				switch (model.currentStateOfPlayerCoordinate(x,y))
				{
					case Ship:
						playerLabels[x][y].setStyle("-fx-background-color: #0004ff;"+"-fx-border-color: #000000");
						break;
					case hit:
						playerLabels[x][y].setStyle("-fx-background-color: #ea1313;"+"-fx-border-color: black");
						break;

					case miss:
						playerLabels[x][y].setStyle("-fx-background-color: #bdbdbd;"+"-fx-border-color: black");
						break;

					default:
						playerLabels[x][y].setStyle("-fx-background-color: #ffffff;"+"-fx-border-color: black");
				}
			}
	}

	public void updateTargetField()
	{
		for(int x=0;x<BOARD_SQUARE_SIZE;x++)
			for(int y=0;y<BOARD_SQUARE_SIZE;y++)
			{
				switch (model.currentStateOfTargetCoordinate(x,y))
				{
					case hit:
						targetLabels[x][y].setStyle("-fx-background-color: #ea1313;"+"-fx-border-color: black");
						break;

					case miss:
						targetLabels[x][y].setStyle("-fx-background-color: #bdbdbd;"+"-fx-border-color: black");
						break;

					default:
						targetLabels[x][y].setStyle("-fx-background-color: #ffffff;"+"-fx-border-color: black");
				}
			}
	}
	public void updateRulesForPhaseChange()
	{

	}
}

package battleship.client.GameWindow;

import battleship.client.Model.CoorrdinateStateEnum;
import battleship.client.Model.GameModel;
import battleship.client.Model.GameStateEnum;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameWindow implements Initializable {

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

	private Scene scene;
	private GameModel model;
	private boolean horizontal;

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

		model= new GameModel();


	}

	private Node getNodeFromGridPane(GridPane gridPane, int xPos, int yPos) {
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
		for (int i=0; i<10;i++)
		{
			for (int j=0; j<10;j++)
			{
				Pane playerPane=new Pane();
				//mouseClick
				playerPane.setOnMouseClicked(event -> {
					event.getSource();
					int xPos = playerGrid.getColumnIndex((Node)event.getSource());
					int yPos = playerGrid.getRowIndex((Node)event.getSource());

					if( model.getCurrentState()==GameStateEnum.setUp)
						model.setShip(xPos,yPos, horizontal);
				});
				//MouseHover
				playerPane.setOnMouseEntered(event->{
					if(model.getCurrentState()==GameStateEnum.setUp) {
						int xPos = playerGrid.getColumnIndex((Node) event.getSource());
						int yPos = playerGrid.getRowIndex((Node) event.getSource());

						if (horizontal)
						{
							if(xPos+model.getTileNumberOfCurrentShip()<10)
							{
								for(int x=0;x<model.getTileNumberOfCurrentShip();x++)
								{
									getNodeFromGridPane(playerGrid,xPos+x,yPos).setStyle("-fx-background-color: #0004ff");
								}
							}
						}
						else
						{
							if(yPos+model.getTileNumberOfCurrentShip()<10)
							{
								for(int y=0;y<model.getTileNumberOfCurrentShip();y++)
								{
									getNodeFromGridPane(playerGrid,xPos,yPos+y).setStyle("-fx-background-color: #0004ff");
								}
							}
						}
					}
				});
				playerPane.setOnMouseExited(event->{
					//selectedShip demarkieren
				});

				playerPane.setOnContextMenuRequested(event->{
					if(model.getCurrentState()==GameStateEnum.setUp)
					{
						int xPos = playerGrid.getColumnIndex((Node) event.getSource());
						int yPos = playerGrid.getRowIndex((Node) event.getSource());

						if (horizontal)
						{

							if(xPos+model.getTileNumberOfCurrentShip()<10)
							{
								for(int x=0;x<model.getTileNumberOfCurrentShip();x++)
								{
									switch(model.currentStateOfPlayerCoordinate(xPos+x,yPos)){
										case Ship:
											getNodeFromGridPane(playerGrid,xPos+x,yPos).setStyle("-fx-background-color: #0004ff");
											break;
										case Empty:
											getNodeFromGridPane(playerGrid,xPos+x,yPos).setStyle("-fx-background-color: #ffffff");
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
											getNodeFromGridPane(playerGrid,xPos,yPos+y).setStyle("-fx-background-color: #0004ff");
											break;
										case Empty:
											getNodeFromGridPane(playerGrid,xPos,yPos+y).setStyle("-fx-background-color: #ffffff");
											break;
									}
								}
							}
						}
						horizontal=(!horizontal);
					}

				});
				playerGrid.add(playerPane,i,j);

				Pane targetPane=new Pane();

				//mouseClick
				targetPane.setOnMouseClicked(event -> {
					event.getSource();
					int xPos = targetGrid.getColumnIndex((Node)event.getSource());
					int yPos = targetGrid.getRowIndex((Node)event.getSource());

					if(model.getCurrentState()== GameStateEnum.shooting)
						model.shootAt(xPos,yPos);
				});

				//MouseHover
				targetPane.setOnMouseEntered(event->{
					if(model.getCurrentState()==GameStateEnum.shooting)
						targetPane.setStyle("-fx-background-color: #e6f54f");
				});

				targetPane.setOnMouseExited(event->{

					if(model.getCurrentState()==GameStateEnum.shooting)
					{
						int xPos = targetGrid.getColumnIndex((Node) event.getSource());
						int yPos = targetGrid.getRowIndex((Node) event.getSource());
						if (model.currentStateOfTargetCoordinate(xPos, yPos) == CoorrdinateStateEnum.Empty)
							targetPane.setStyle("-fx-background-color: #ffffff");
						if (model.currentStateOfTargetCoordinate(xPos, yPos) == CoorrdinateStateEnum.hit)
							targetPane.setStyle("-fx-background-color: #ea1313");
						if (model.currentStateOfTargetCoordinate(xPos, yPos) == CoorrdinateStateEnum.miss)
							targetPane.setStyle("-fx-background-color: #bdbdbd");
					}
				});
				targetGrid.add(targetPane,i,j);
			}
		}
	}
}

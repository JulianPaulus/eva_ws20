package battleship.client.Model;

import javafx.fxml.FXML;

import java.util.Arrays;

public class GameModel {

	ShipType currentShip;
	boolean placedTwice;

	private GameStateEnum currentState;
	private CoorrdinateStateEnum[][] playerField;
	private CoorrdinateStateEnum[][] targetField;

	public GameModel() {
		currentState = GameStateEnum.setUp;
		playerField = new CoorrdinateStateEnum[10][10];
		targetField = new CoorrdinateStateEnum[10][10];

		for (int i = 0; i < 10; i++) {
			Arrays.fill(playerField[i], CoorrdinateStateEnum.Empty);
			Arrays.fill(targetField[i], CoorrdinateStateEnum.Empty);
		}

		currentShip=ShipType.twoTiles;
	}

	public int getTileNumberOfCurrentShip()
	{
		int current= currentShip.getValue();

		switch (currentShip){
			case twoTiles:
				currentShip=ShipType.threeTiles;
				break;
			case threeTiles:
				if(placedTwice)
					currentShip=ShipType.forTiles;
				else
					placedTwice=true;
				break;
			case forTiles:
				currentShip=ShipType.fiveTiles;
				break;
			case fiveTiles:
				currentState=GameStateEnum.shooting;
		}
		return current;
	}

	public void setShip(int xPos, int yPos, boolean horizontal)
	{
		if(horizontal)
		{
			if( xPos+currentShip.getValue()<=10)
				for (int i=0; i<currentShip.getValue();i++)
					playerField[xPos+i][yPos]=CoorrdinateStateEnum.Ship;
		}
		else
		{
			if( yPos+currentShip.getValue()<=10)
				for (int i=0; i<currentShip.getValue();i++)
					playerField[xPos][yPos+i]=CoorrdinateStateEnum.Ship;
		}
	}
	public GameStateEnum getCurrentState() {
		return currentState;
	}

	public void setCurrentState(GameStateEnum currentState) {
		this.currentState = currentState;
	}

	public void shootAt(int xPos,int yPos)
	{
		if(targetField[xPos][yPos]!=CoorrdinateStateEnum.hit || targetField[xPos][yPos]!=CoorrdinateStateEnum.miss)
		{
			//Anbindung an Web, wenn antwort Feld entsprechend auf hit/miss setzen

			currentState=GameStateEnum.waitingforEnemy;
		}
	}
	public void shootIncomingAt(int xPos,int yPos){

		if (playerField[xPos][yPos]==CoorrdinateStateEnum.Ship)
			playerField[xPos][yPos]=CoorrdinateStateEnum.hit;

		currentState=GameStateEnum.shooting;
	}
	public CoorrdinateStateEnum currentStateOfTargetCoordinate(int xPos, int yPos)
	{
		return targetField[xPos][yPos];
	}
	public CoorrdinateStateEnum currentStateOfPlayerCoordinate(int xPos, int yPos)
	{
		return playerField[xPos][yPos];
	}

	public void sendChatMessage(String message)
	{

	}
}

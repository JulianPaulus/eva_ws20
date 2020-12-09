package battleships.client.Model;

import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GameModel {

	ShipType currentShip;
	boolean placedTwice;

	private GameStateEnum currentState;
	private CoorrdinateStateEnum[][] playerField;
	private CoorrdinateStateEnum[][] targetField;
	private List<String> chat;
	private Ship[] ships;
	private Ship lastAdded;

	private ModelObserver observer;

	public GameModel(ModelObserver observer) {
		currentState = GameStateEnum.setUp;
		playerField = new CoorrdinateStateEnum[10][10];
		targetField = new CoorrdinateStateEnum[10][10];
		chat=new LinkedList<>();
		ships=new Ship[5];

		for (int i = 0; i < 10; i++) {
			Arrays.fill(playerField[i], CoorrdinateStateEnum.Empty);
			Arrays.fill(targetField[i], CoorrdinateStateEnum.Empty);
		}

		currentShip=ShipType.twoTiles;

		this.observer=observer;
	}

	public int getTileNumberOfCurrentShip()
	{
		return currentShip.getValue();
	}

	public void setShip(int xPos, int yPos, boolean horizontal)
	{

		Ship ship=new Ship(currentShip,xPos,yPos,horizontal);

		if(!checkForShipAvailability(ship))
			return;
		//send to Server, if success do the following

		for (int i=0;i<5;i++)
		{
			if(ships[i]==null) {
				ships[i] = ship;
				break;
			}
		}

		if(horizontal)
		{
			if( xPos+currentShip.getValue()<=10) {
				for (int i = 0; i < currentShip.getValue(); i++)
					playerField[xPos + i][yPos] = CoorrdinateStateEnum.Ship;
			}

		}
		else
		{
			if( yPos+currentShip.getValue()<=10)
				for (int i=0; i<currentShip.getValue();i++)
					playerField[xPos][yPos+i]=CoorrdinateStateEnum.Ship;
		}

		lastAdded=ship;
		switchToNextBiggerShipType();

		observer.notifyAboutPlayerModelChange();
	}

	private boolean checkForShipAvailability(Ship ship)
	{
		int xPos=ship.getxCoordinate();
		int yPos=ship.getyCoordinate();

		if(ship.isHorizontal()) {
			if (playerField[xPos-1][yPos]==CoorrdinateStateEnum.Ship ||playerField[xPos+ship.getType().getValue()][yPos]==CoorrdinateStateEnum.Ship)
				return false;
		}
		else {
			if (playerField[xPos][yPos-1]==CoorrdinateStateEnum.Ship ||playerField[xPos][yPos+ship.getType().getValue()]==CoorrdinateStateEnum.Ship)
				return false;
		}

		for(int i=0;i<ship.getType().getValue();i++)
		{
			if(ship.isHorizontal()) {
				if (playerField[xPos+i][yPos]==CoorrdinateStateEnum.Ship ||
					playerField[xPos+i][yPos+1]==CoorrdinateStateEnum.Ship||
					playerField[xPos+i][yPos-1]==CoorrdinateStateEnum.Ship)
					return false;
			}
			else {
				if (playerField[xPos][yPos+i]==CoorrdinateStateEnum.Ship ||
					playerField[xPos+1][yPos+i]==CoorrdinateStateEnum.Ship||
					playerField[xPos-i][yPos+i]==CoorrdinateStateEnum.Ship)
					return false;
			}
		}

		return true;
	}

	public GameStateEnum getCurrentState() {
		return currentState;
	}

	public void setCurrentState(GameStateEnum currentState) {
		this.currentState = currentState;
		observer.notifyAboutGameStatusChange();
	}

	public void shootAt(int xPos,int yPos)
	{
		if(targetField[xPos][yPos]!=CoorrdinateStateEnum.hit || targetField[xPos][yPos]!=CoorrdinateStateEnum.miss)
		{
			//Anbindung an Web, wenn antwort Feld entsprechend auf hit/miss setzen

		}
	}

	public void setHitOrMissAt(int xPos,int yPos, CoorrdinateStateEnum status)
	{
		targetField[xPos][yPos]=status;

		observer.notifyAboutTargetModelChange();
		currentState=GameStateEnum.waitingforEnemy;
	}

	public void shootIncomingAt(int xPos,int yPos){

		if (playerField[xPos][yPos]==CoorrdinateStateEnum.Ship) {
			playerField[xPos][yPos] = CoorrdinateStateEnum.hit;

			for (Ship ship:ships) {
				if(ship.isHorizontal()&&yPos==ship.getyCoordinate()&&xPos>=ship.getxCoordinate()&&xPos<=ship.getxCoordinate()+ship.getType().getValue())
					ship.hit();
				else if(ship.isHorizontal()&&xPos==ship.getxCoordinate()&&yPos>=ship.getyCoordinate()&&yPos<=ship.getyCoordinate()+ship.getType().getValue())
					ship.hit();

				if(ship.isDestroyed())
					observer.notifyAboutDestroyedShip();
			}
		}
		observer.notifyAboutPlayerModelChange();
		currentState=GameStateEnum.shooting;
		observer.notifyAboutGameStatusChange();
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
		//sende Nachricht an Server

		receiveChatMessage(message);		//testweise bitte Löschen wenn anbindung an Server
	}

	public void receiveChatMessage(String message)
	{
		chat.add(message);
		observer.notifyAboutChatMessage();
	}

	public List<String> getChatMessages()
	{
		return chat;
	}

	public void switchToNextBiggerShipType()
	{
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
				observer.notifyAboutGameStatusChange();
				break;
		}
		observer.notifyAboutShipTypeChange();
	}

	public void switchToPreviousShipType()
	{
		switch (currentShip){
			case twoTiles:
				currentShip=ShipType.twoTiles;
				break;
			case threeTiles:
				if(placedTwice)
					currentShip=ShipType.twoTiles;
				else
					placedTwice=false;
				break;
			case forTiles:
				currentShip=ShipType.threeTiles;
				break;
			case fiveTiles:
				currentShip=ShipType.forTiles;
				break;
		}
		observer.notifyAboutShipTypeChange();
	}

	public void removeLastAdded()
	{
		int xPos= lastAdded.getxCoordinate();
		int yPos= lastAdded.getyCoordinate();

		int shipLength=lastAdded.getType().getValue();

		for(int i=0; i< shipLength;i++)
		{
			if (lastAdded.isHorizontal()) {
				playerField[xPos + i][yPos] = CoorrdinateStateEnum.Empty;
			} else {
				playerField[xPos][yPos + i] = CoorrdinateStateEnum.Empty;
			}

		}

		for(int i=0;i<ships.length;i++)
		{
			if(ships[i].equals(lastAdded)) {
				ships[i] = null;

				if(i>0)
				{
					lastAdded=ships[i-1];
				}
				switchToPreviousShipType();
				break;
			}
		}

		observer.notifyAboutPlayerModelChange();
	}

	public void setPlayerLost()
	{
		currentState=GameStateEnum.lost;
		observer.notifyAboutGameStatusChange();
	}

	public void setPlayerWon()
	{
		currentState=GameStateEnum.won;
		observer.notifyAboutGameStatusChange();
	}
}

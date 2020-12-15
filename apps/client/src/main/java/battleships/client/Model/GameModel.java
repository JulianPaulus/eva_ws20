package battleships.client.Model;

import battleships.model.CoordinateState;
import battleships.model.Ship;
import battleships.model.ShipType;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GameModel {

	ShipType currentShip;
	boolean placedTwice;

	private GameState currentState;
	private CoordinateState[][] playerField;
	private CoordinateState[][] targetField;
	private List<String> chat;
	private Ship[] ships;
	private Ship lastAdded;

	private ModelObserver observer;

	public GameModel(ModelObserver observer) {
		currentState = GameState.PENDING;
		playerField = new CoordinateState[10][10];
		targetField = new CoordinateState[10][10];
		chat=new LinkedList<>();
		ships=new Ship[5];

		for (int i = 0; i < 10; i++) {
			Arrays.fill(playerField[i], CoordinateState.EMPTY);
			Arrays.fill(targetField[i], CoordinateState.EMPTY);
		}

		currentShip=ShipType.TWO_TILES;

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
					playerField[xPos + i][yPos] = CoordinateState.SHIP;
			}

		}
		else
		{
			if( yPos+currentShip.getValue()<=10)
				for (int i=0; i<currentShip.getValue();i++)
					playerField[xPos][yPos+i]= CoordinateState.SHIP;
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
			if (playerField[xPos-1][yPos]== CoordinateState.SHIP ||playerField[xPos+ship.getType().getValue()][yPos]== CoordinateState.SHIP)
				return false;
		}
		else {
			if (playerField[xPos][yPos-1]== CoordinateState.SHIP ||playerField[xPos][yPos+ship.getType().getValue()]== CoordinateState.SHIP)
				return false;
		}

		for(int i=0;i<ship.getType().getValue();i++)
		{
			if(ship.isHorizontal()) {
				if (playerField[xPos+i][yPos]== CoordinateState.SHIP ||
					playerField[xPos+i][yPos+1]== CoordinateState.SHIP ||
					playerField[xPos+i][yPos-1]== CoordinateState.SHIP)
					return false;
			}
			else {
				if (playerField[xPos][yPos+i]== CoordinateState.SHIP ||
					playerField[xPos+1][yPos+i]== CoordinateState.SHIP ||
					playerField[xPos-1][yPos+i]== CoordinateState.SHIP)
					return false;
			}
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

	public void shootAt(int xPos,int yPos)
	{
		if(targetField[xPos][yPos]!= CoordinateState.HIT || targetField[xPos][yPos]!= CoordinateState.MISS)
		{
			//Anbindung an Web, wenn antwort Feld entsprechend auf hit/miss setzen

		}
	}

	public void setHitOrMissAt(int xPos,int yPos, CoordinateState status)
	{
		targetField[xPos][yPos]=status;

		observer.notifyAboutTargetModelChange();
		currentState= GameState.WAIT_FOR_ENEMY;
	}

	public void shootIncomingAt(int xPos,int yPos){

		if (playerField[xPos][yPos]== CoordinateState.SHIP) {
			playerField[xPos][yPos] = CoordinateState.HIT;

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
		currentState= GameState.SHOOTING;
		observer.notifyAboutGameStatusChange();
	}
	public CoordinateState currentStateOfTargetCoordinate(int xPos, int yPos)
	{
		return targetField[xPos][yPos];
	}
	public CoordinateState currentStateOfPlayerCoordinate(int xPos, int yPos)
	{
		return playerField[xPos][yPos];
	}

	public void receiveChatMessage(final String fromUser, final String message)
	{
		chat.add(fromUser + ": "+ message);
		observer.notifyAboutChatMessage();
	}

	public List<String> getChatMessages()
	{
		return chat;
	}

	public void switchToNextBiggerShipType()
	{
		switch (currentShip){
			case TWO_TILES:
				currentShip=ShipType.THREE_TILES;
				break;
			case THREE_TILES:
				if(placedTwice)
					currentShip=ShipType.FOUR_TILES;
				else
					placedTwice=true;
				break;
			case FOUR_TILES:
				currentShip=ShipType.FIVE_TILES;
				break;
			case FIVE_TILES:
				currentState= GameState.PENDING;
				observer.notifyAboutGameStatusChange();
				break;
		}
		observer.notifyAboutShipTypeChange();
	}

	public void switchToPreviousShipType()
	{
		switch (currentShip){
			case TWO_TILES:
				currentShip=ShipType.TWO_TILES;
				break;
			case THREE_TILES:
				if(placedTwice)
					currentShip=ShipType.TWO_TILES;
				else
					placedTwice=false;
				break;
			case FOUR_TILES:
				currentShip=ShipType.THREE_TILES;
				break;
			case FIVE_TILES:
				currentShip=ShipType.FOUR_TILES;
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
				playerField[xPos + i][yPos] = CoordinateState.EMPTY;
			} else {
				playerField[xPos][yPos + i] = CoordinateState.EMPTY;
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
		currentState= GameState.LOST;
		observer.notifyAboutGameStatusChange();
	}

	public void setPlayerWon()
	{
		currentState= GameState.WON;
		observer.notifyAboutGameStatusChange();
	}


}

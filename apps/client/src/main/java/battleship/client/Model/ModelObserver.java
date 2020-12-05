package battleship.client.Model;

import battleship.client.GameWindow.GameWindow;

public class ModelObserver {
	GameWindow view;

	public ModelObserver(GameWindow view) {
		this.view = view;
	}

	public void notifyAboutChatMessage(){

	}

	public void notifyAboutTargetModelChange()
	{

	}

	public void notifyAboutPlayerModelChange()
	{

	}

	public void notifyAboutGameStatusChange()
	{

	}

	public void notifyAboutShipTypeChange()
	{

	}

	public void notifyAboutDestroyedShip()
	{

	}

	public void notifyAllChanged()
	{

	}
}

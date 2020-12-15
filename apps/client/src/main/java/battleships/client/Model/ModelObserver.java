package battleships.client.Model;

import battleships.client.GameWindow.GameWindow;

public class ModelObserver {
	GameWindow view;

	public ModelObserver(GameWindow view) {
		this.view = view;
	}

	public void notifyAboutChatMessage(){

		view.updateChatWindow();
	}

	public void notifyAboutTargetModelChange()
	{
		view.updateTargetField();
	}

	public void notifyAboutPlayerModelChange()
	{
		view.updatePlayerField();
	}

	public void notifyAboutGameStatusChange()
	{
		view.updateRulesForPhaseChange();
	}

	public void notifyAboutShipTypeChange()	//eventuell nicht notwendig,da der Schiffstüp immer aktuell aus dem Model geholt wird
	{

	}

	public void notifyAboutDestroyedShip()
	{

	}

	public void notifyAllChanged()
	{
		view.updateChatWindow();
		view.updateTargetField();
		view.updatePlayerField();
	}
}

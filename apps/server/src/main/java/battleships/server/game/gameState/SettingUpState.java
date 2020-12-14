package battleships.server.game.gameState;

public class SettingUpState implements ServerGameState {

	@Override
	public boolean canSetShip() {
		return true;
	}

	@Override
	public boolean canTransition(final ServerGameState newState) {
		return newState instanceof UninitializedState;
	}
}

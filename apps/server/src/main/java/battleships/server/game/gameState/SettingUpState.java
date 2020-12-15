package battleships.server.game.gameState;

public class SettingUpState implements ServerGameState {

	@Override
	public boolean canHostSetShip() {
		return true;
	}

	@Override
	public boolean canGuestSetShip() {
		return true;
	}

	@Override
	public boolean canTransition(final ServerGameState newState) {
		return newState instanceof UninitializedState
			|| newState instanceof SettingUpGuestState
			|| newState instanceof SettingUpHostState;
	}
}

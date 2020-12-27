package battleships.server.game.gameState;

public class UninitializedState implements ServerGameState {

	@Override
	public boolean isInitialized() {
		return false;
	}

	@Override
	public boolean canTransition(final ServerGameState newState) {
		return newState instanceof UninitializedState || newState instanceof SettingUpState;
	}
}

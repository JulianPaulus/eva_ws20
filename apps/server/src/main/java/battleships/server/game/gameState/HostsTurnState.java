package battleships.server.game.gameState;

public class HostsTurnState implements ServerGameState {

	@Override
	public boolean canHostFire() {
		return true;
	}

	@Override
	public boolean canTransition(final ServerGameState newState) {
		return newState instanceof UninitializedState || newState instanceof GuestsTurnState || newState instanceof GameFinishedState;
	}

}

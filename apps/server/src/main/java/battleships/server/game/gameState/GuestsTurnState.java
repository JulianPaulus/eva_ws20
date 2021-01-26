package battleships.server.game.gameState;

public class GuestsTurnState implements ServerGameState {

	@Override
	public boolean canGuestFire() {
		return true;
	}

	@Override
	public boolean canTransition(final ServerGameState newState) {
		return newState instanceof UninitializedState || newState instanceof HostsTurnState || newState instanceof GameFinishedState;
	}
}

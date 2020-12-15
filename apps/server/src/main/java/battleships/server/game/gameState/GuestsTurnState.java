package battleships.server.game.gameState;

public class GuestsTurnState implements ServerGameState {

	public boolean canGuestFire() {
		return true;
	}

	@Override
	public boolean canTransition(final ServerGameState newState) {
		return newState instanceof HostsTurnState;
	}
}

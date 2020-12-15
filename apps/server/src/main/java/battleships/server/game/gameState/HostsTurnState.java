package battleships.server.game.gameState;

public class HostsTurnState implements ServerGameState {

	public boolean canHostFire() {
		return true;
	}

	@Override
	public boolean canTransition(final ServerGameState newState) {
		return newState instanceof GuestsTurnState;
	}

}

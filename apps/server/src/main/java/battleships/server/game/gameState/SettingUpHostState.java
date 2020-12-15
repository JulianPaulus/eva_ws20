package battleships.server.game.gameState;

public class SettingUpHostState implements ServerGameState {

	public boolean canGuestSetShip() {
		return true;
	}

	@Override
	public boolean canTransition(final ServerGameState newState) {
		return newState instanceof HostsTurnState;
	}

}

package battleships.server.game.gameState;

public interface ServerGameState {

	default boolean isInitialized() {
		return true;
	}

	default boolean isWaitingForGuest() {
		return false;
	}

	default boolean canHostSetShip() {
		return false;
	}

	default boolean canGuestSetShip() {
		return false;
	}

	default boolean canHostFire() {
		return false;
	}

	default boolean canGuestFire() {
		return false;
	}

	default boolean canVoteRematch() {
		return false;
	}

	default boolean canTransition(final ServerGameState newState) {
		return newState instanceof UninitializedState;
	}

}

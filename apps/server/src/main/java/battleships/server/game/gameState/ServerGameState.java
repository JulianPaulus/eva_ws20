package battleships.server.game.gameState;

public interface ServerGameState {

	default boolean isInitialized() {
		return true;
	}

	default boolean isWaitingForGuest() {
		return false;
	}

	default boolean canChat() {
		return true;
	}

	default boolean canSetShip() {
		return false;
	}

	default boolean canFire() {
		return false;
	}

	default boolean canTransition(final ServerGameState newState) {
		return false;
	}

}

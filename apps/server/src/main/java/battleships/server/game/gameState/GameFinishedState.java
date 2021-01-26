package battleships.server.game.gameState;

import java.util.HashMap;
import java.util.Map;

public class GameFinishedState implements ServerGameState {

	private static final int REQUIRED_REMATCH_VOTES = 2;

	private final Map<String, Boolean> rematchVotes = new HashMap<>();

	@Override
	public boolean canVoteRematch() {
		return true;
	}

	@Override
	public boolean canTransition(final ServerGameState newState) {
		return newState instanceof UninitializedState || newState instanceof SettingUpState;
	}

	public void addVote(final String byUser) {
		rematchVotes.put(byUser, true);
	}

	public boolean canStartRematch() {
		return rematchVotes.size() == REQUIRED_REMATCH_VOTES;
	}
}

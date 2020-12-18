package battleships.client.Model;

public enum GameState {
	PENDING,
	SET_UP,
	SET_UP_WAIT_FOR_OTHER_PLAYER,
	SHOOTING,
	SHOOTING_WAIT_FOR_RESPONSE,
	WAIT_FOR_ENEMY,
	WON,
	LOST
}

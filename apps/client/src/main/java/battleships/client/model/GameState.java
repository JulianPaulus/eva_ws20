package battleships.client.model;

import battleships.client.game.GameWindow;
import battleships.client.util.GameStateRunnable;
import battleships.client.util.StringProperties;
import javafx.application.Platform;

public enum GameState {
	PENDING(defaultRunnableWithButtonVisible(false)),

	SET_UP(defaultRunnableWithButtonVisible(true)),

	SET_UP_WAIT_FOR_OTHER_PLAYER(defaultRunnableWithButtonVisible(false)),

	SHOOTING(defaultRunnableWithButtonVisible(false)),

	SHOOTING_WAIT_FOR_RESPONSE(defaultRunnableWithButtonVisible(false)),

	WAIT_FOR_ENEMY(defaultRunnableWithButtonVisible(false)),

	WON((controller) -> {
		controller.updateStatusText();
		controller.updateRulesText();
		controller.setStatusLabelStyle("-fx-text-fill: green");
		controller.setRemoveShipButtonVisible(false);
		controller.setRematchButtonActive(true);
	}),

	LOST((controller) -> {
		controller.updateStatusText();
		controller.updateRulesText();
		controller.setStatusLabelStyle("-fx-text-fill: red");
		controller.setRemoveShipButtonVisible(false);
		controller.setRematchButtonActive(true);
	}),

	AUTOMATIC_WIN((controller) -> {
		defaultRunnableWithButtonVisible(false).run(controller);
		controller.setStatusLabelStyle("-fx-text-fill: black");
		controller.disableChat();
	}),

	OTHER_PLAYER_DISCONNECTED((controller) -> {
		defaultRunnableWithButtonVisible(false).run(controller);
		controller.setStatusLabelStyle("-fx-text-fill: black");
		controller.disableChat();
	}),

	WAITING_FOR_REMATCH((controller) -> {
		controller.updateStatusText();
		controller.updateRulesText();
		controller.setStatusLabelStyle("-fx-text-fill: black");
	});

	private static GameStateRunnable defaultRunnableWithButtonVisible(final boolean visible) {
		return (controller) -> {
			controller.updateStatusText();
			controller.updateRulesText();
			controller.setRemoveShipButtonVisible(visible);
			controller.setRematchButtonActive(false);
		};
	}

	private static final StringProperties STRING_PROPERTIES = StringProperties.getInstance();

	private final GameStateRunnable runnable;

	GameState(final GameStateRunnable runnable) {
		this.runnable = runnable;
	}

	public String getStatusText() {
		return STRING_PROPERTIES.get(this.toString() + ".status");
	}

	public String getRuleText() {
		return STRING_PROPERTIES.get(this.toString() + ".rule");
	}

	public void updateGameWindow(final GameWindow controller) {
		Platform.runLater(() -> {
			runnable.run(controller);
		});
	}
}

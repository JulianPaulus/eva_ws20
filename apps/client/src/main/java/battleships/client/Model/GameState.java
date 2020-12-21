package battleships.client.Model;

import battleships.client.GameWindow.GameWindow;
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
	}),

	LOST((controller) -> {
		controller.updateStatusText();
		controller.updateRulesText();
		controller.setStatusLabelStyle("-fx-text-fill: red");
		controller.setRemoveShipButtonVisible(false);
	}),

	AUTOMATIC_WIN((controller) -> {
		defaultRunnableWithButtonVisible(false).run(controller);
		controller.disableChat();
	}),

	OTHER_PLAYER_DISCONNECTED(GameWindow::disableChat);

	private static GameStateRunnable defaultRunnableWithButtonVisible(final boolean visible) {
		return (controller) -> {
			controller.updateStatusText();
			controller.updateRulesText();
			controller.setRemoveShipButtonVisible(visible);
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

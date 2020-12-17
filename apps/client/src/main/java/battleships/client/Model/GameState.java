package battleships.client.Model;

import battleships.client.GameWindow.GameWindow;
import battleships.client.util.GameStateRunnable;
import battleships.client.util.StringProperties;
import javafx.application.Platform;

public enum GameState {
	PENDING((controller) -> {
		controller.updateStatusText();
		controller.updateRulesText();
		controller.setRemoveShipButtonVisible(false);
	}),

	SET_UP((controller) -> {
		controller.updateStatusText();
		controller.updateRulesText();
		controller.setRemoveShipButtonVisible(true);
	}),

	SET_UP_WAIT_FOR_OTHER_PLAYER((controller) -> {
		controller.updateStatusText();
		controller.updateRulesText();
		controller.setRemoveShipButtonVisible(false);
	}),

	SHOOTING((controller) -> {
		controller.updateStatusText();
		controller.updateRulesText();
		controller.setRemoveShipButtonVisible(false);
	}),

	WAIT_FOR_ENEMY((controller) -> {
		controller.updateStatusText();
		controller.updateRulesText();
		controller.setRemoveShipButtonVisible(false);
	}),

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
	});

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

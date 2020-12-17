package battleships.client.Model;

import battleships.client.GameWindow.GameWindow;
import battleships.client.util.GameStateRunnable;
import javafx.application.Platform;
import org.apache.logging.log4j.util.Strings;

public enum GameState {
	PENDING((controller) -> {
		controller.setStatusLabel("Warte auf anderen Spieler...");
		controller.updateRulesText("Bitte warten Sie, bis ein anderer Spieler dem Spiel beitritt!");
		controller.setRemoveShipButtonVisible(false);
	}),

	SET_UP((controller) -> {
		controller.setStatusLabel("Bitte Schiffe setzen");
		controller.updateRulesText("setzen sie ihre Schiffe:\n" +
			"Beim Hovern \u00FCber dem Spielfeld wird die derzeitige Position des Schiffs angezeigt.\n" +
			"Mit Rechtsklick \u00E4ndern sie die Ausrichtung (Horizontal/Vertikal)\n" +
			"Mit linksklick setzen sie das Schiff\n" +
			"Schiffe werden Blau dargestellt");
		controller.setRemoveShipButtonVisible(true);
	}),

	SET_UP_WAIT_FOR_OTHER_PLAYER((controller) -> {
		controller.setStatusLabel("Warte auf andern Spieler...");
		controller.updateRulesText("Ihre Gegner hat noch nicht alle Schiffe platziert.\n" +
			"Das Spiel startet automatisch, sobald Ihre Gegner seine Schiffe platziert hat!");
		controller.setRemoveShipButtonVisible(false);
	}),

	SHOOTING((controller) -> {
		controller.setStatusLabel("Bitte Zielen");
		controller.updateRulesText(
			"Klicken sie auf das Zielen spielfeld, um auf die gew\u00FCnschte Position zu schie\u00DFen.\n" +
				"Treffer werden rot dargestellt, Verfehlungen werden grau dargestellt");
		controller.setRemoveShipButtonVisible(false);
	}),

	WAIT_FOR_ENEMY((controller) -> {
		controller.setStatusLabel("Warten auf Gegner");
		controller.updateRulesText("Der Gegner schie\u00DFt, bitte warten.\n" +
			"Treffer auf ihren Schiffen werden rot dargestellt, Verfehlungen werden grau dargestellt");
		controller.setRemoveShipButtonVisible(false);
	}),

	WON((controller) -> {
		controller.setStatusLabel("Gewonnen");
		controller.setStatusLabelStyle("-fx-text-fill: green");
		controller.updateRulesText(Strings.EMPTY);
		controller.setRemoveShipButtonVisible(false);
	}),

	LOST((controller) -> {
		controller.setStatusLabel("Verloren");
		controller.setStatusLabelStyle("-fx-text-fill: red");
		controller.updateRulesText(Strings.EMPTY);
		controller.setRemoveShipButtonVisible(false);
	});

	private final GameStateRunnable runnable;

	GameState(final GameStateRunnable runnable) {
		this.runnable = runnable;
	}

	public void updateGameWindow(final GameWindow controller) {
		Platform.runLater(() -> {
			runnable.run(controller);
		});
	}
}

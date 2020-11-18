package battleships.client.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ErrorDialog {

	public static void show(Stage owner, String message) {
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.initOwner(owner);
			alert.setHeaderText("Ein Fehler ist aufgetreten:");
			alert.setContentText(message);
			alert.show();
		});
	}

}

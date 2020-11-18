package battleships.client.packet.receive;

import battleships.client.ClientMain;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;
import battleships.util.RegistrationError;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Modality;

public class RegistrationErrorResponsePacket implements IPreAuthReceivePacket {

	public static final byte IDENTIFIER = Constants.Identifiers.REGISTER_ERROR_RESPONSE;

	private final boolean successful;
	private final RegistrationError registrationError;
	private final String message;

	public RegistrationErrorResponsePacket(final boolean successful, final RegistrationError registrationError, final String message) {
		this.successful = successful;
		this.registrationError = registrationError;
		this.message = message;
	}

	@Override
	public void act(final Connection connection) {
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.initOwner(ClientMain.getInstance().getStage());
			alert.setHeaderText("Ein Fehler ist aufgetreten:");
			alert.setContentText(message);
			alert.show();
		});
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}
}

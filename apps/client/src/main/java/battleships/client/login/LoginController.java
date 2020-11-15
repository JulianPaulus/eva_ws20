package battleships.client.login;

import battleships.Constants;
import battleships.client.ClientMain;
import battleships.client.packet.send.LoginPacket;
import battleships.client.packet.send.RegisterPacket;
import battleships.net.connection.Connection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController {

	/**
	 * This pattern should match all possible addresses
	 * 1st Capture group matches the hostname/ip:
	 * 	google.com
	 * 	google.co.uk
	 * 	localhost
	 * 	192.168.0.1
	 * 2nd Capture group matches the port (with :) if it is contained within the string
	 * 3rd Capture group matches ports with 4-5 digits (without the :)
	 */
	private static final Pattern ADDRESS_PATTTERN = Pattern
		.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|localhost|.*\\..{2,3})(:(\\d{4,5}))?");

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	private ClientMain client;

	@FXML
	private TextField addressField;
	@FXML
	private TextField nameField;
	@FXML
	private PasswordField passwordField;

	@FXML
	private void initialize() {
		client = ClientMain.getInstance();

		setAddressValidator();
	}

	@FXML
	private void onLogin() {
		connect(new LoginPacket(nameField.getText(), passwordField.getText()));
	}

	@FXML
	private void onRegister() {
		connect(new RegisterPacket(nameField.getText(), passwordField.getText()));
	}

	private void connect(final LoginPacket packet) {
		Pair<String, Integer> address = decodeAddress();
		Thread connectThread = new Thread(() -> {
			try {
				Connection connection = client.connect(address.getKey(), address.getValue());
				connection.writePacket(packet);
			} catch (IOException e) {
				logger.trace("unable to connect to the server", e);

				Platform.runLater(() -> {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.initModality(Modality.APPLICATION_MODAL);
					alert.initOwner(client.getStage());
					alert.setHeaderText("Verbindung fehlgeschlagen");
					alert.setContentText(e.getMessage());
					alert.show();
				});
			}
		});
		connectThread.start();
	}

	private void setAddressValidator() {
		addressField.focusedProperty().addListener(((observable, oldValue, newValue) -> {
			Matcher matcher = ADDRESS_PATTTERN.matcher(addressField.getText());
			if (!matcher.matches()) {
				if (!addressField.getStyleClass().contains("error")) {
					addressField.getStyleClass().add("error");
				}
			} else {
				addressField.getStyleClass().remove("error");
			}
		}));
	}

	private Pair<String, Integer> decodeAddress() {
		Matcher matcher = ADDRESS_PATTTERN.matcher(addressField.getText());
		if (matcher.matches()) {
			String host = matcher.group(0);
			Integer port = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : Constants.DEFAULT_PORT;

			return new Pair<>(host, port);
		}
		return new Pair<>(null, null);
	}

}

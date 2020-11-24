package battleships.client.login;

import battleships.client.ClientMain;
import battleships.client.packet.send.LoginPacket;
import battleships.client.packet.send.RegisterPacket;
import battleships.client.util.ErrorDialog;
import battleships.net.connection.Connection;
import battleships.util.Constants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
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
		.compile("^(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|localhost|.*\\.[a-zA-Z]{2,3})(:([1-9]\\d{3,4}))?$");

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	private ClientMain client;

	@FXML
	private TextField addressField;
	@FXML
	private TextField nameField;
	@FXML
	private PasswordField passwordField;

	@FXML
	private Button registerButton;

	@FXML
	private Button loginButton;

	@FXML
	private void initialize() {
		client = ClientMain.getInstance();
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
		loginButton.setDisable(true);
		registerButton.setDisable(true);
		Thread connectThread = new Thread(() -> {
			try {
				Connection connection = client.connect(address.getKey(), address.getValue());
				connection.writePacket(packet);
			} catch (final IOException e) {
				logger.trace("unable to connect to the server", e);
				ErrorDialog.show(client.getStage(), "Verbindung fehlgeschlagen!");
			} finally {
				Platform.runLater(() -> {
					boolean buttonState = getButtonDisabledState();
					loginButton.setDisable(buttonState);
					registerButton.setDisable(buttonState);
				});
			}
		});
		connectThread.start();
	}

	@FXML
	private void onFieldChange(final KeyEvent keyEvent) {
		TextField source = (TextField) keyEvent.getSource();

		boolean isFieldValid;
		if(source.equals(addressField)) {
			isFieldValid = ADDRESS_PATTTERN.matcher(source.getText().trim()).matches();
		} else {
			isFieldValid = !source.getText().trim().isEmpty();
		}

		if(!isFieldValid) {
			if (!source.getStyleClass().contains("error")) {
				source.getStyleClass().add("error");
			}
		} else {
			source.getStyleClass().remove("error");
		}

		boolean buttonState = getButtonDisabledState();
		registerButton.setDisable(buttonState);
		loginButton.setDisable(buttonState);
	}

	private boolean getButtonDisabledState() {
		return !ADDRESS_PATTTERN.matcher(addressField.getText().trim()).matches()
			|| nameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty();
	}

	private Pair<String, Integer> decodeAddress() {
		Matcher matcher = ADDRESS_PATTTERN.matcher(addressField.getText().trim());
		if (matcher.matches()) {
			String host = matcher.group(1);
			Integer port = matcher.group(3) != null ? Integer.parseInt(matcher.group(3)) : Constants.DEFAULT_PORT;

			return new Pair<>(host, port);
		}
		return new Pair<>(null, null);
	}

}

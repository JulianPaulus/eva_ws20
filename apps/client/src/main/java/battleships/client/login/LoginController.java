package battleships.client.login;

import battleships.client.ClientMain;
import battleships.client.packet.send.LoginPacket;
import battleships.net.connection.Connection;
import battleships.net.connection.Constants;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Pair;

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

	private ClientMain client;

	@FXML
	private TextField addressField;
	@FXML
	private TextField nameField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Button loginButton;

	@FXML
	public void initialize() {
		client = ClientMain.getInstance();

		addressField.textProperty().addListener(((observable, oldValue, newValue) -> {
			Matcher matcher = ADDRESS_PATTTERN.matcher(addressField.getText());
			if (!matcher.matches()) {
				System.err.println("doesnt match");
			} else {
				System.err.println("matches");
			}
		}));
	}

	@FXML
	private void onLogin() {
		Pair<String, Integer> address = decodeAddress();
		try {
			Connection connection = client.connect(address.getKey(), address.getValue());
			connection.writePacket(new LoginPacket(nameField.getText(), passwordField.getText()));
		} catch (IOException e) {
			e.printStackTrace();
		}
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

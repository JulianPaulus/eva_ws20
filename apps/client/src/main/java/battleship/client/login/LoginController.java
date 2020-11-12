package battleship.client.login;

import battleship.client.ClientMain;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Pair;

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
	}

	@FXML
	private void onLogin() {

	}

	private Pair<String, Integer> decodeAddress() {
		return new Pair<>(null, null);
	}

}

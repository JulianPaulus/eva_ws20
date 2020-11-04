package battleship.client.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

	@FXML
	private TextField ipField;
	@FXML
	private TextField userField;
	@FXML
	private PasswordField pwField;
	@FXML
	private Button connectBtn;

	@FXML
	private void onConnect() {
		System.out.println(ipField.getText());
	}

}

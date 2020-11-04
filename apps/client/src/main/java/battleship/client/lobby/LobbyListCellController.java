package battleship.client.lobby;

import battleship.packet.Lobby;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class LobbyListCellController extends ListCell<Lobby> {

	@FXML
	private Label nameLabel;

	@FXML
	private Label stateLabel;

	@FXML
	private Button joinButton;

	@FXML
	private BorderPane borderPane;

	private FXMLLoader fxmLLoader;

	@Override
	protected void updateItem(Lobby lobby, boolean empty) {
		super.updateItem(lobby, empty);

		setText(null);
		if(empty || lobby == null) {
			setGraphic(null);
		} else {
			if(fxmLLoader == null) {
				fxmLLoader = new FXMLLoader(getClass().getResource("/fxml/LobbyListCell.fxml"));
				fxmLLoader.setController(this);
				try {
					fxmLLoader.load();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			nameLabel.setText(lobby.getName());
			if(lobby.getMaxPlayers() > lobby.getPlayerCount()) {
				stateLabel.setText(lobby.getPlayerCount() + "/" + lobby.getMaxPlayers() + " Warte auf Spieler...");
				joinButton.setVisible(true);
			} else {
				stateLabel.setText("Im Spiel");
				joinButton.setVisible(false);
			}
			setGraphic(borderPane);
		}
	}
}

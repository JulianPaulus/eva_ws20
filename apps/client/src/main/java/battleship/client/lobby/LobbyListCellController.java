package battleship.client.lobby;

import battleship.packet.PacketLobby;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class LobbyListCellController extends ListCell<PacketLobby> {
	private static final int LOBBY_MAX_PLAYERS = 2;

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
	protected void updateItem(PacketLobby lobby, boolean empty) {
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
			stateLabel.setText(1 + "/" + LOBBY_MAX_PLAYERS + " Warte auf Spieler...");
			joinButton.setVisible(true);
			setGraphic(borderPane);
		}
	}
}

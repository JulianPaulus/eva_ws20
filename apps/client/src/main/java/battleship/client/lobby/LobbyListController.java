package battleship.client.lobby;

import battleship.client.ClientMain;
import battleship.net.packet.LobbyListRequestPacket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.*;

public class LobbyListController implements Initializable {

	@FXML
	private ListView<String> listView;

	@FXML
	private Button createLobbyButton;

	private ObservableList<String> lobbyObservableList;

	public LobbyListController() {
		LobbyListRequestPacket lobbyListRequestPacket = new LobbyListRequestPacket();
		ClientMain.getInstance().getConnection().writePacket(lobbyListRequestPacket);
		lobbyObservableList = FXCollections.observableArrayList();
	}


	public void onClickCreateLobbyButton(ActionEvent actionEvent) {
		if(actionEvent.getSource() != this.createLobbyButton) {
			return;
		}

		//TODO Implement
	}

	public void setLobbies(Collection<String> lobbies) {
		lobbyObservableList.setAll(lobbies);
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listView.setItems(lobbyObservableList);
		listView.setCellFactory(x -> new LobbyListCellController());
	}
}

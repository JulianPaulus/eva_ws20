package battleship.client.lobby;

import battleship.packet.Lobby;
import battleship.packet.Player;
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
	private ListView<Lobby> listView;

	@FXML
	private Button createLobbyButton;

	private ObservableList<Lobby> lobbyObservableList;

	public LobbyListController() {
		lobbyObservableList = FXCollections.observableArrayList();
		lobbyObservableList.addAll(new Lobby("Test 1", Arrays.asList(new Player(1, "Player 1"), new Player(2, "Player 2"))),
			new Lobby("Test 2", Arrays.asList(new Player(3, "Player 1"))));
	}


	public void onClickCreateLobbyButton(ActionEvent actionEvent) {
		if(actionEvent.getSource() != this.createLobbyButton) {
			return;
		}

		//TODO Implement
	}

	public void setLobbies(Collection<Lobby> lobbies) {
		lobbyObservableList.setAll(lobbies);
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listView.setItems(lobbyObservableList);
		listView.setCellFactory(x -> new LobbyListCellController());
	}
}

package battleship.client.lobby;

import battleship.client.Client;
import battleship.client.packet.send.LobbyListRequestPacket;
import battleship.iface.ILobbyListController;
import battleship.packet.PacketLobby;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public class LobbyListController implements Initializable, ILobbyListController {

	@FXML
	private ListView<PacketLobby> listView;

	@FXML
	private Button createLobbyButton;

	private ObservableList<PacketLobby> lobbyObservableList;

	private static LobbyListController newestInstance;

	public LobbyListController() {
		LobbyListRequestPacket lobbyListRequestPacket = new LobbyListRequestPacket();
		try {
			Client.getInstance().getConnection().writePacket(lobbyListRequestPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		lobbyObservableList = FXCollections.observableArrayList();
		newestInstance = this;
	}

	public static LobbyListController getNewestInstance() {
		return newestInstance;
	}


	public void onClickCreateLobbyButton(ActionEvent actionEvent) {
		if(actionEvent.getSource() != this.createLobbyButton) {
			return;
		}

		//TODO Implement
	}

	public void setLobbies(Collection<PacketLobby> lobbies) {
		lobbyObservableList.setAll(lobbies);
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listView.setItems(lobbyObservableList);
		listView.setCellFactory(x -> new LobbyListCellController());
	}
}

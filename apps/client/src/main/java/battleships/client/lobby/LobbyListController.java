package battleships.client.lobby;

import battleships.client.ClientMain;
import battleships.client.packet.send.LobbyListRequestPacket;
import battleships.packet.PacketLobby;
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

public class LobbyListController implements Initializable {

	@FXML
	private ListView<PacketLobby> listView;

	@FXML
	private Button createLobbyButton;

	private final ObservableList<PacketLobby> lobbyObservableList;

	private static LobbyListController newestInstance;

	public LobbyListController() {
		LobbyListRequestPacket lobbyListRequestPacket = new LobbyListRequestPacket();
		try {
			ClientMain.getInstance().getConnection().writePacket(lobbyListRequestPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		lobbyObservableList = FXCollections.observableArrayList();
		newestInstance = this;
	}

	public static LobbyListController getNewestInstance() {
		return newestInstance;
	}


	public void onClickCreateLobbyButton(final ActionEvent actionEvent) {
		if(actionEvent.getSource() != this.createLobbyButton) {
			return;
		}

		//Implement
	}

	public void setLobbies(final Collection<PacketLobby> lobbies) {
		lobbyObservableList.setAll(lobbies);
	}


	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		listView.setItems(lobbyObservableList);
		listView.setCellFactory(x -> new LobbyListCellController());
	}
}

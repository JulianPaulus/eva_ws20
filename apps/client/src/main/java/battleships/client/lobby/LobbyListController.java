package battleships.client.lobby;

import battleships.client.ClientMain;
import battleships.client.GameWindow.GameWindow;
import battleships.client.login.LoginController;
import battleships.client.packet.send.CreateGamePacket;
import battleships.client.packet.send.LobbyListRequestPacket;
import battleships.model.PacketLobby;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

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
		ClientMain.getInstance().getConnection().writePacket(lobbyListRequestPacket);
		lobbyObservableList = FXCollections.observableArrayList();
		newestInstance = this;
	}

	public static void openWindow(Stage stage) {
		FXMLLoader loader = new FXMLLoader();
		try {
			loader.load(LoginController.class.getResourceAsStream("/fxml/LobbyListScreen.fxml"));
			Scene scene = new Scene(loader.getRoot());
			scene.getStylesheets().add(GameWindow.class.getResource("/fxml/style.css").toString());
			Platform.runLater(() -> {
				stage.setScene(scene);
				stage.show();
			});
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static LobbyListController getNewestInstance() {
		return newestInstance;
	}


	@FXML
	public void onClickCreateLobbyButton(final ActionEvent actionEvent) {
		if(actionEvent.getSource() != this.createLobbyButton) {
			return;
		}

		ClientMain.getInstance().getConnection().writePacket(new CreateGamePacket());
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

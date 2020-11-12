package battleship.client.packet.receive;

import battleship.client.ClientMain;
import battleship.net.connection.Connection;
import battleship.net.packet.IPreAuthReceivePacket;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class LoginResponsePacket implements IPreAuthReceivePacket {

	public static final byte IDENTIFIER = 0x4;

	private final int playerId;
	private final boolean successful;

	public LoginResponsePacket(int playerId, boolean successful) {
		this.playerId = playerId;
		this.successful = successful;
	}

	@Override
	public void act(Connection connection) {
		System.out.println("response");
		System.out.println(playerId);
		System.out.println(successful);
		if (successful) {
			try {
				final Parent root = FXMLLoader.load(getClass().getResource("/fxml/LobbyListScreen.fxml"));
				Platform.runLater(() -> {
					ClientMain.getInstance().getStage().setScene(new Scene(root));
					ClientMain.getInstance().getStage().show();
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	public int getPlayerId() {
		return playerId;
	}

	public boolean isSuccessful() {
		return successful;
	}
}

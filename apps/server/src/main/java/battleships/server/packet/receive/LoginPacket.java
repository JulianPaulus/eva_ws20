package battleships.server.packet.receive;

import battleship.net.connection.AuthenticatedConnection;
import battleship.net.connection.Connection;
import battleship.net.packet.AbstractPacket;
import battleship.packet.Player;
import battleship.server.service.PlayerService;
import battleships.server.packet.send.LoginResponsePacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class LoginPacket extends AbstractPacket<Connection> {
	public static final byte IDENTIFIER = 0x3;

	private final String username;
	private final String password;

	public LoginPacket(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected DataOutputStream writeContent(DataOutputStream dos) throws IOException {
		return dos;
		//Nothing
	}

	@Override
	public void act(Connection connection) {
		Player player = PlayerService.getInstance().authenticate(username, password);
		if (player != null) {
			AuthenticatedConnection authedConnection = new AuthenticatedConnection(connection, player);
			try {
				authedConnection.writePacket(new LoginResponsePacket(player.getId(), true));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				connection.writePacket(new LoginResponsePacket(-1, false));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

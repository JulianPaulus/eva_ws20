package battleship.net.packet.server;

import battleship.net.ConnectionSide;
import battleship.net.connection.AuthenticatedConnection;
import battleship.net.connection.Connection;
import battleship.net.packet.AbstractPacket;
import battleship.net.packet.client.LoginResponsePacket;
import battleship.packet.Player;
import battleship.server.service.PlayerService;

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

	@Override
	public ConnectionSide getConnectionSide() {
		return ConnectionSide.SERVER;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	protected DataOutputStream writeContent(DataOutputStream dos) throws IOException {
		dos.writeUTF(username);
		dos.writeUTF(password);

		return dos;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}

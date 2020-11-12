package battleships.server.packet.receive;

import battleships.net.connection.AuthenticatedConnection;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.packet.Player;
import battleships.server.packet.send.LoginResponsePacket;
import battleships.server.service.PlayerService;

import java.io.IOException;

public class LoginPacket implements IPreAuthReceivePacket {
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

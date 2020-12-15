package battleships.server.packet.receive;

import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.server.connection.AuthenticatedConnection;
import battleships.server.game.Player;
import battleships.server.packet.send.LoginResponsePacket;
import battleships.server.service.PlayerService;
import battleships.util.Constants;

import javax.security.auth.login.LoginException;

public class LoginPacket implements IPreAuthReceivePacket {
	public static final byte IDENTIFIER = Constants.Identifiers.LOGIN_REQUEST;

	private final String username;
	private final String password;

	public LoginPacket(final String username, final String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(final Connection connection) {
		Player player = null;
		try {
			player = PlayerService.getInstance().authenticate(username, password);
		} catch (final LoginException e) {
			connection.writePacket(new LoginResponsePacket(-1, false));
			connection.close();
			return;
		}

		AuthenticatedConnection authedConnection = new AuthenticatedConnection(connection, player);
		authedConnection.writePacket(new LoginResponsePacket(player.getId(), true));
	}
}

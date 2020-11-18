package battleships.server.packet.receive;

import battleships.net.connection.AuthenticatedConnection;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.packet.Player;
import battleships.server.exception.RegistrationException;
import battleships.server.packet.send.LoginResponsePacket;
import battleships.server.packet.send.RegistrationErrorResponsePacket;
import battleships.server.service.PlayerService;
import battleships.util.Constants;

public class RegisterPacket implements IPreAuthReceivePacket {

	public static final byte IDENTIFIER = Constants.Identifiers.REGISTER_REQUEST;
	private static final PlayerService PLAYER_SERVICE = PlayerService.getInstance();

	private final String username;
	private final String password;

	public RegisterPacket(final String username, final String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public void act(final Connection connection) {
		try {
			Player player = PLAYER_SERVICE.register(username, password);
			AuthenticatedConnection authedConnection = new AuthenticatedConnection(connection, player);
			authedConnection.writePacket(new LoginResponsePacket(player.getId(), true));
		} catch (final RegistrationException e) {
			connection.writePacket(new RegistrationErrorResponsePacket(false, e.getRegistrationError(), e.getMessage()));
		}
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}
}

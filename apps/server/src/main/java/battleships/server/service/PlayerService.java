package battleships.server.service;

import battleships.server.connection.AuthenticatedConnection;
import battleships.server.db.UserDatabase;
import battleships.server.exception.RegistrationException;
import battleships.server.game.Player;
import battleships.server.util.PasswordHasher;

import javax.security.auth.login.LoginException;
import java.util.Optional;

public class PlayerService {

	private static PlayerService instance;
	private final UserDatabase userDatabase = UserDatabase.getInstance();
	private final ConnectionService connectionService = ConnectionService.getInstance();

	private PlayerService() {

	}

	public Player authenticate(final String username, final String password) throws LoginException {
		Player player = userDatabase.loadPlayerByName(username);
		if(player == null || !PasswordHasher.checkPassword(password, player.getPassword())) {
			throw new LoginException("Username or password wrong");
		}
		Optional<AuthenticatedConnection> connection = connectionService.getConnectionForPlayer(player);
		if (connection.isPresent()) {
			throw new LoginException("User already logged in");
		}
		if (!connectionService.canAcceptConnections()) {
			throw new LoginException("Server is full");
		}
		return player;
	}

	public Player register(final String username, final String password) throws RegistrationException {
		Player player = new Player(0, username, PasswordHasher.hashPassword(password, PasswordHasher.genSalt()));
		return userDatabase.savePlayer(player);
	}


	public synchronized static PlayerService getInstance() {
		if (instance == null) {
			instance = new PlayerService();
		}
		return instance;
	}


}

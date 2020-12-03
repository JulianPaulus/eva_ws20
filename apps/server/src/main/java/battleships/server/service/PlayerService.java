package battleships.server.service;

import battleships.server.db.UserDatabase;
import battleships.server.exception.RegistrationException;
import battleships.server.game.Player;
import battleships.server.util.PasswordHasher;

import javax.security.auth.login.LoginException;

public class PlayerService {

	private static PlayerService instance;
	private UserDatabase userDatabase = UserDatabase.getInstance();

	private PlayerService() {

	}

	public Player authenticate(final String username, final String password) throws LoginException {
		Player player = userDatabase.loadPlayerByName(username);
		if(player == null || !PasswordHasher.checkPassword(password, player.getPassword())) {
			throw new LoginException("Username or password wrong");
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

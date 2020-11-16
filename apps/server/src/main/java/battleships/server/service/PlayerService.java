package battleships.server.service;

import battleships.packet.Player;
import battleships.server.exception.RegistrationException;

public class PlayerService {

	private static PlayerService instance;

	private PlayerService() {

	}

	public Player authenticate(final String username, final String password) {
		return new Player((int) (Math.random() * 10000), username);
	}

	public Player register(final String username, final String password) throws RegistrationException {
		return authenticate(username, password);
	}


	public synchronized static PlayerService getInstance() {
		if (instance == null) {
			instance = new PlayerService();
		}
		return instance;
	}


}

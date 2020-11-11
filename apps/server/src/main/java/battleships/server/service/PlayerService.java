package battleships.server.service;

import battleship.packet.Player;

public class PlayerService {

	private static PlayerService instance;

	private PlayerService() {

	}

	public Player authenticate(String username, String password) {
		return new Player((int) (Math.random() * 10000), username);
	}


	public synchronized static PlayerService getInstance() {
		if (instance == null) {
			instance = new PlayerService();
		}
		return instance;
	}


}

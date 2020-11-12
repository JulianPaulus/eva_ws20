package battleship.server.service;

public class LobbyService {

	private static LobbyService instance;


	public synchronized static LobbyService getInstance() {
		if (instance == null) {
			instance = new LobbyService();
		}
		return instance;
	}
}

package battleship.packet;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
	private final static int MAX_PLAYERS_PER_LOBBY = 2;
	private String name;
	private List<Player> players = new ArrayList<Player>();

	public Lobby(String name, List<Player> players) {
		this.name = name;
		this.players = players;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public boolean isFull() {
		return MAX_PLAYERS_PER_LOBBY >= players.size();
	}

	public int getMaxPlayers() {
		return MAX_PLAYERS_PER_LOBBY;
	}

	public int getPlayerCount() {
		return players.size();
	}
}

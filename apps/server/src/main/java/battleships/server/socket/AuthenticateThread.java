package battleships.server.socket;

import battleship.net.packet.AbstractPacket;
import battleship.net.packet.LoginPacket;
import battleship.packet.Player;
import battleship.util.AuthenticatedConnection;
import battleship.util.Connection;
import battleships.server.service.PlayerService;

public class AuthenticateThread extends Thread {

	private final Connection connection;
	private boolean loggedIn;

	private final PlayerService playerService;
	private final LobbyThread lobbyThread;

	public AuthenticateThread(final Connection connection) {
		this.connection = connection;
		this.loggedIn = false;
		this.playerService = PlayerService.getInstance();
		this.lobbyThread = LobbyThread.getInstance();
	}

	@Override
	public void run() {
		while (!isInterrupted() && !loggedIn)
		try {
			AbstractPacket packet = connection.readPacketBlocking();

			if (packet instanceof LoginPacket) {
				LoginPacket loginPacket = (LoginPacket) packet;
				Player player = playerService.authenticate(loginPacket.getUsername(), loginPacket.getPassword());
				if (player != null) {
					lobbyThread.addConnection(new AuthenticatedConnection(connection, player));
					loggedIn = true;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

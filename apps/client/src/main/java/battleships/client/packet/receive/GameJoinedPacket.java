package battleships.client.packet.receive;

import battleships.client.ClientMain;
import battleships.client.GameWindow.GameWindow;
import battleships.net.connection.Connection;
import battleships.net.packet.IPreAuthReceivePacket;
import battleships.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class GameJoinedPacket implements IPreAuthReceivePacket {

	private static final Logger LOGGER = LoggerFactory.getLogger(GameJoinedPacket.class);
	public static final byte IDENTIFIER = Constants.Identifiers.GAME_JOIN_RESPONSE;

	private final UUID gameId;

	public GameJoinedPacket(final UUID gameId) {
		this.gameId = gameId;
	}

	@Override
	public byte getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void act(final Connection connection) {
		LOGGER.debug("joining game {}", gameId.toString());
		CountDownLatch latch = new CountDownLatch(1);
		GameWindow.openWindow(ClientMain.getInstance().getStage(), latch);
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

package battleships.server.packet.receive.factory;

import battleships.model.Ship;
import battleships.net.factory.AbstractPacketFactory;
import battleships.server.packet.receive.PlayerReadyPacket;
import battleships.util.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class PlayerReadyPacketFactory extends AbstractPacketFactory<PlayerReadyPacket> {

	@Override
	public PlayerReadyPacket unmarshal(DataInputStream stream) throws IOException {
		Ship[] ships = new Ship[Constants.SHIP_COUNT];
		for(int i = 0; i < ships.length; i++) {
			ships[i] = Ship.fromStream(stream);
		}
		return new PlayerReadyPacket(ships);
	}
}

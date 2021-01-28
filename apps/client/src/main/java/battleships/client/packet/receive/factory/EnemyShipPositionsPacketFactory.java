package battleships.client.packet.receive.factory;

import battleships.client.packet.receive.EnemyShipPositionsPacket;
import battleships.model.Ship;
import battleships.net.factory.AbstractPacketFactory;
import battleships.util.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class EnemyShipPositionsPacketFactory extends AbstractPacketFactory<EnemyShipPositionsPacket> {
	@Override
	public EnemyShipPositionsPacket unmarshal(final DataInputStream stream) throws IOException {
		Ship[] ships = new Ship[Constants.SHIP_COUNT];
		for(int i = 0; i < ships.length; i++) {
			ships[i] = Ship.fromStream(stream);
		}
		return new EnemyShipPositionsPacket(ships);
	}
}

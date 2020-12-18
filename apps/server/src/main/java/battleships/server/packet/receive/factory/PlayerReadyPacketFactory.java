package battleships.server.packet.receive.factory;

import battleships.model.Ship;
import battleships.model.ShipType;
import battleships.net.factory.AbstractPacketFactory;
import battleships.server.packet.receive.PlayerReadyPacket;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class PlayerReadyPacketFactory extends AbstractPacketFactory<PlayerReadyPacket> {

	@Override
	public PlayerReadyPacket unmarshal(DataInputStream stream) throws IOException {
		Ship[] ships = new Ship[5];
		for(int i = 0; i < ships.length; i++) {
			int shipSize = stream.readInt();
			Optional<ShipType> oShipType = Arrays.stream(ShipType.values())
				.filter(x -> x.getSize() == shipSize).findFirst();
			if(!oShipType.isPresent()) {
				//TODO error
			}
			ships[i] = new Ship(oShipType.get(), stream.readInt(), stream.readInt(), stream.readBoolean());
		}
		return new PlayerReadyPacket(ships);
	}
}

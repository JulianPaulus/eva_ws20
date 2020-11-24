package battleships.server.packet.receive.factory;

import battleships.net.factory.AbstractPacketFactory;
import battleships.server.packet.receive.RegisterPacket;

import java.io.DataInputStream;
import java.io.IOException;

public class RegisterPacketFactory extends AbstractPacketFactory<RegisterPacket> {
	@Override
	public RegisterPacket unmarshal(final DataInputStream stream) throws IOException {
		return new RegisterPacket(stream.readUTF(), stream.readUTF());
	}
}


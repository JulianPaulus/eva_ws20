package battleships.client.packet.receive.factory;

import battleships.client.packet.receive.RegistrationErrorResponsePacket;
import battleships.net.factory.AbstractPacketFactory;
import battleships.util.RegistrationError;

import java.io.DataInputStream;
import java.io.IOException;

public class RegistrationErrorResponseFactory extends AbstractPacketFactory<RegistrationErrorResponsePacket> {
	@Override
	public RegistrationErrorResponsePacket unmarshal(final DataInputStream stream) throws IOException {
		return new RegistrationErrorResponsePacket(stream.readBoolean(), RegistrationError.valueOf(stream.readUTF()));
	}
}

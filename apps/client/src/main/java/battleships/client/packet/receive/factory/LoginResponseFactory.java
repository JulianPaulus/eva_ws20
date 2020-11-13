package battleships.client.packet.receive.factory;


import battleships.client.packet.receive.LoginResponsePacket;
import battleships.net.factory.AbstractPacketFactory;

import java.io.DataInputStream;
import java.io.IOException;

public class LoginResponseFactory extends AbstractPacketFactory<LoginResponsePacket> {
	@Override
	public LoginResponsePacket unmarshal(final DataInputStream stream) throws IOException {
		return new LoginResponsePacket(stream.readInt(), stream.readBoolean());
	}
}

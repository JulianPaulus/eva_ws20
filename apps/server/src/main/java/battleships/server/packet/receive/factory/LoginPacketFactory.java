package battleships.server.packet.receive.factory;

import battleships.net.factory.AbstractPacketFactory;
import battleships.server.packet.receive.LoginPacket;

import java.io.DataInputStream;
import java.io.IOException;

public class LoginPacketFactory extends AbstractPacketFactory<LoginPacket> {
	@Override
	public LoginPacket unmarshal(final DataInputStream stream) throws IOException {
		return new LoginPacket(stream.readUTF(), stream.readUTF());
	}
}

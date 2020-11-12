package battleship.client.packet.receive.factory;


import battleship.client.packet.receive.LoginResponsePacket;
import battleship.net.factory.AbstractPacketFactory;

import java.io.DataInputStream;
import java.io.IOException;

public class LoginResponseFactory extends AbstractPacketFactory<LoginResponsePacket> {
	@Override
	public LoginResponsePacket unmarshal(DataInputStream stream) throws IOException {
		return new LoginResponsePacket(stream.readInt(), stream.readBoolean());
	}
}

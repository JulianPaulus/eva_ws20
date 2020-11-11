package battleship.net.factory;

import battleship.net.packet.client.LoginResponsePacket;

import java.io.DataInputStream;
import java.io.IOException;

public class LoginResponseFactory extends AbstractPacketFactory<LoginResponsePacket> {
	@Override
	public LoginResponsePacket unmarshal(DataInputStream stream) throws IOException {
		return new LoginResponsePacket(stream.readInt(), stream.readBoolean());
	}
}

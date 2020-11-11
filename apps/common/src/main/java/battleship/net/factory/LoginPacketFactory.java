package battleship.net.factory;

import battleship.net.packet.server.LoginPacket;

import java.io.DataInputStream;
import java.io.IOException;

public class LoginPacketFactory extends AbstractPacketFactory<LoginPacket> {
	@Override
	public LoginPacket unmarshal(DataInputStream stream) throws IOException {
		return new LoginPacket(stream.readUTF(), stream.readUTF());
	}
}

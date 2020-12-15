package battleships.client.packet.receive.factory;

import battleships.client.packet.receive.ServerErrorPacket;
import battleships.net.factory.AbstractPacketFactory;
import battleships.util.ServerErrorType;

import java.io.DataInputStream;
import java.io.IOException;

public class ServerErrorPacketFactory extends AbstractPacketFactory<ServerErrorPacket> {
	@Override
	public ServerErrorPacket unmarshal(final DataInputStream stream) throws IOException {
		ServerErrorType type = ServerErrorType.valueOf(stream.readUTF());
		return new ServerErrorPacket(type, stream.readUTF());
	}
}

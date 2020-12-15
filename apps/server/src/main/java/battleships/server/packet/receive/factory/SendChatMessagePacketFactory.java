package battleships.server.packet.receive.factory;

import battleships.net.factory.AbstractPacketFactory;
import battleships.server.packet.receive.SendChatMessagePacket;

import java.io.DataInputStream;
import java.io.IOException;

public class SendChatMessagePacketFactory extends AbstractPacketFactory<SendChatMessagePacket> {
	@Override
	public SendChatMessagePacket unmarshal(final DataInputStream stream) throws IOException {
		return new SendChatMessagePacket(stream.readUTF());
	}
}

package battleships.client.packet.receive.factory;

import battleships.client.packet.receive.ChatMessagePacket;
import battleships.net.factory.AbstractPacketFactory;

import java.io.DataInputStream;
import java.io.IOException;

public class ChatMessagePacketFactory extends AbstractPacketFactory<ChatMessagePacket> {
	@Override
	public ChatMessagePacket unmarshal(final DataInputStream stream) throws IOException {
		return new ChatMessagePacket(stream.readUTF(), stream.readUTF());
	}
}

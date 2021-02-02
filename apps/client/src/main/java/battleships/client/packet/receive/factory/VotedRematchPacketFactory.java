package battleships.client.packet.receive.factory;

import battleships.client.packet.receive.VotedRematchPacket;
import battleships.net.factory.AbstractPacketFactory;

import java.io.DataInputStream;
import java.io.IOException;

public class VotedRematchPacketFactory extends AbstractPacketFactory<VotedRematchPacket> {
	@Override
	public VotedRematchPacket unmarshal(DataInputStream stream) throws IOException {
		return new VotedRematchPacket(stream.readUTF());
	}
}

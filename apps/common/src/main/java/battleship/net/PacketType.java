package battleship.net;

import battleship.net.factory.AbstractPacketFactory;
import battleship.net.factory.TestPacketFactory;
import battleship.net.packet.TestPacket;

import java.util.Arrays;
import java.util.Optional;

public enum PacketType {

	TEST(TestPacket.IDENTIFIER, new TestPacketFactory());

	private final byte identifier;
	private final AbstractPacketFactory factory;

	PacketType(final byte identifier, final AbstractPacketFactory factory) {
		this.identifier = identifier;
		this.factory = factory;
	}

	public byte getIdentifier() {
		return identifier;
	}

	public AbstractPacketFactory getFactory() {
		return factory;
	}

	public static Optional<PacketType> getByIdentifier(final byte identifier) {
		return Arrays.stream(PacketType.values()).filter(type -> type.identifier == identifier).findFirst();
	}
}

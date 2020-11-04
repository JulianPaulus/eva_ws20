package battleship.net;

import battleship.net.factory.PacketFactory;
import battleship.net.factory.TestPacketFactory;

import java.util.Arrays;
import java.util.Optional;

public enum PacketType {

	TEST((byte) 0x0, new TestPacketFactory());

	private final byte identifier;
	private final PacketFactory factory;

	PacketType(byte identifier, PacketFactory factory) {
		this.identifier = identifier;
		this.factory = factory;
	}

	public byte getIdentifier() {
		return identifier;
	}

	public PacketFactory getFactory() {
		return factory;
	}

	public static Optional<PacketType> getByIdentifier(byte identifier) {
		return Arrays.stream(PacketType.values()).filter(type -> type.identifier == identifier).findFirst();
	}
}

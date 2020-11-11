package battleship.net;

import battleship.net.factory.AbstractPacketFactory;
import battleship.net.factory.LobbyListPacketFactory;
import battleship.net.factory.LobbyListRequestPacketFactory;
import battleship.net.factory.LoginPacketFactory;
import battleship.net.factory.TestPacketFactory;
import battleship.net.packet.AbstractPacket;
import battleship.net.packet.LobbyListPacket;
import battleship.net.packet.LobbyListRequestPacket;
import battleship.net.packet.LoginPacket;
import battleship.net.packet.TestPacket;

import java.util.Arrays;
import java.util.Optional;

public enum PacketType {

	TEST(TestPacket.IDENTIFIER, new TestPacketFactory()),
	LOBBY_LIST(LobbyListPacket.IDENTIFIER, new LobbyListPacketFactory()),
	LOBBY_LIST_REQUEST(LobbyListRequestPacket.IDENTIFIER, new LobbyListRequestPacketFactory()),
	LOGIN(LoginPacket.IDENTIFIER, new LoginPacketFactory());

	private final byte identifier;
	private final AbstractPacketFactory<? extends AbstractPacket> factory;

	PacketType(final byte identifier, final AbstractPacketFactory<? extends AbstractPacket> factory) {
		this.identifier = identifier;
		this.factory = factory;
	}

	public byte getIdentifier() {
		return identifier;
	}

	public AbstractPacketFactory<? extends AbstractPacket> getFactory() {
		return factory;
	}

	public static Optional<PacketType> getByIdentifier(final byte identifier) {
		return Arrays.stream(PacketType.values()).filter(type -> type.identifier == identifier).findFirst();
	}
}

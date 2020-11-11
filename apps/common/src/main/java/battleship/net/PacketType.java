package battleship.net;

import battleship.net.factory.AbstractPacketFactory;
import battleship.net.factory.LobbyListPacketFactory;
import battleship.net.factory.LobbyListRequestPacketFactory;
import battleship.net.factory.LoginPacketFactory;
import battleship.net.factory.LoginResponseFactory;
import battleship.net.factory.TestPacketFactory;
import battleship.net.packet.AbstractPacket;
import battleship.net.packet.TestPacket;
import battleship.net.packet.client.LobbyListPacket;
import battleship.net.packet.client.LoginResponsePacket;
import battleship.net.packet.server.LobbyListRequestPacket;
import battleship.net.packet.server.LoginPacket;

import java.util.Arrays;
import java.util.Optional;

public enum PacketType {

	TEST(TestPacket.IDENTIFIER, new TestPacketFactory()),
	LOBBY_LIST(LobbyListPacket.IDENTIFIER, new LobbyListPacketFactory()),
	LOBBY_LIST_REQUEST(LobbyListRequestPacket.IDENTIFIER, new LobbyListRequestPacketFactory()),
	LOGIN(LoginPacket.IDENTIFIER, new LoginPacketFactory()),
	LOGIN_RESPONSE(LoginResponsePacket.IDENTIFIER, new LoginResponseFactory());

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

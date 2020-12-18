package battleships.server;

import battleships.net.connection.PacketReader;
import battleships.net.factory.AbstractPacketFactory;
import battleships.server.packet.receive.*;
import battleships.server.packet.receive.factory.*;
import battleships.server.socket.Server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServerMain {

	static {
		Map<Byte, AbstractPacketFactory<?>> packetFactoryMap = new HashMap<>();
		packetFactoryMap.put(LobbyListRequestPacket.IDENTIFIER, new LobbyListRequestPacketFactory());
		packetFactoryMap.put(LoginPacket.IDENTIFIER, new LoginPacketFactory());
		packetFactoryMap.put(RegisterPacket.IDENTIFIER, new RegisterPacketFactory());
		packetFactoryMap.put(CreateGamePacket.IDENTIFIER, new CreateGamePacketFactory());
		packetFactoryMap.put(JoinGamePacket.IDENTIFIER, new JoinGamePacketFactory());
		packetFactoryMap.put(SendChatMessagePacket.IDENTIFIER, new SendChatMessagePacketFactory());
		packetFactoryMap.put(PlayerReadyPacket.IDENTIFIER, new PlayerReadyPacketFactory());
		PacketReader.setFactoryMap(packetFactoryMap);
	}


	public static void main(final String[] args) throws IOException {
		Server server = Server.getInstance();

		server.start();
	}

}

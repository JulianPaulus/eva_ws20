package battleships.server;

import battleships.net.connection.PacketReader;
import battleships.net.factory.AbstractPacketFactory;
import battleships.server.packet.receive.CreateGamePacket;
import battleships.server.packet.receive.JoinGamePacket;
import battleships.server.packet.receive.LobbyListRequestPacket;
import battleships.server.packet.receive.LoginPacket;
import battleships.server.packet.receive.RegisterPacket;
import battleships.server.packet.receive.SendChatMessagePacket;
import battleships.server.packet.receive.factory.CreateGamePacketFactory;
import battleships.server.packet.receive.factory.JoinGamePacketFactory;
import battleships.server.packet.receive.factory.LobbyListRequestPacketFactory;
import battleships.server.packet.receive.factory.LoginPacketFactory;
import battleships.server.packet.receive.factory.RegisterPacketFactory;
import battleships.server.packet.receive.factory.SendChatMessagePacketFactory;
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
		PacketReader.setFactoryMap(packetFactoryMap);
	}


	public static void main(final String[] args) throws IOException {
		Server server = Server.getInstance();

		server.start();
	}

}

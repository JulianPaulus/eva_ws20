package battleships.server;

import battleship.net.connection.PacketReader;
import battleship.net.factory.AbstractPacketFactory;
import battleship.server.socket.Server;
import battleships.server.packet.receive.LobbyListRequestPacket;
import battleships.server.packet.receive.LoginPacket;
import battleships.server.packet.receive.factory.LobbyListRequestPacketFactory;
import battleships.server.packet.receive.factory.LoginPacketFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServerMain {

	static {
		Map<Byte, AbstractPacketFactory<?>> packetFactoryMap = new HashMap<>();
		packetFactoryMap.put(LobbyListRequestPacket.IDENTIFIER, new LobbyListRequestPacketFactory());
		packetFactoryMap.put(LoginPacket.IDENTIFIER, new LoginPacketFactory());
		PacketReader.setFactoryMap(packetFactoryMap);
	}


	public static void main(String... args) throws IOException {
		Server server = Server.getInstance();

		server.start();
	}

}

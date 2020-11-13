package battleships.server;

import battleships.net.connection.PacketReader;
import battleships.net.factory.AbstractPacketFactory;
import battleships.net.factory.TestPacketFactory;
import battleships.net.packet.TestPacket;
import battleships.server.packet.receive.LobbyListRequestPacket;
import battleships.server.packet.receive.LoginPacket;
import battleships.server.packet.receive.factory.LobbyListRequestPacketFactory;
import battleships.server.packet.receive.factory.LoginPacketFactory;
import battleships.server.socket.Server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServerMain {

	static {
		Map<Byte, AbstractPacketFactory<?>> packetFactoryMap = new HashMap<>();
		packetFactoryMap.put(TestPacket.IDENTIFIER, new TestPacketFactory());
		packetFactoryMap.put(LobbyListRequestPacket.IDENTIFIER, new LobbyListRequestPacketFactory());
		packetFactoryMap.put(LoginPacket.IDENTIFIER, new LoginPacketFactory());
		PacketReader.setFactoryMap(packetFactoryMap);
	}


	public static void main(final String[] args) throws IOException {
		Server server = Server.getInstance();

		server.start();
	}

}

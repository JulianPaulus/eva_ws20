package battleships.server;

import battleships.net.connection.PacketReader;
import battleships.net.factory.AbstractPacketFactory;
import battleships.net.factory.StateLessPacketFactory;
import battleships.net.packet.HeartbeatPacket;
import battleships.server.cli.Argument;
import battleships.server.cli.ArgumentParser;
import battleships.server.exception.ArgumentParseException;
import battleships.server.packet.receive.CreateGamePacket;
import battleships.server.packet.receive.JoinGamePacket;
import battleships.server.packet.receive.LobbyListRequestPacket;
import battleships.server.packet.receive.LoginPacket;
import battleships.server.packet.receive.PlayerReadyPacket;
import battleships.server.packet.receive.RegisterPacket;
import battleships.server.packet.receive.SendChatMessagePacket;
import battleships.server.packet.receive.ShootPacket;
import battleships.server.packet.receive.VoteRematchPacket;
import battleships.server.packet.receive.factory.CreateGamePacketFactory;
import battleships.server.packet.receive.factory.JoinGamePacketFactory;
import battleships.server.packet.receive.factory.LobbyListRequestPacketFactory;
import battleships.server.packet.receive.factory.LoginPacketFactory;
import battleships.server.packet.receive.factory.PlayerReadyPacketFactory;
import battleships.server.packet.receive.factory.RegisterPacketFactory;
import battleships.server.packet.receive.factory.SendChatMessagePacketFactory;
import battleships.server.packet.receive.factory.ShootPacketFactory;
import battleships.server.socket.Server;
import battleships.server.socket.ServerConfig;

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
		packetFactoryMap.put(ShootPacket.IDENTIFIER, new ShootPacketFactory());
		packetFactoryMap.put(HeartbeatPacket.IDENTIFIER, new StateLessPacketFactory<>(HeartbeatPacket.class));
		packetFactoryMap.put(VoteRematchPacket.IDENTIFIER, new StateLessPacketFactory<>(VoteRematchPacket.class));
		PacketReader.setFactoryMap(packetFactoryMap);
	}


	public static void main(final String[] args) throws IOException {

		ArgumentParser argumentParser = new ArgumentParser();
		argumentParser.addArgument(new Argument<>("--port", "port to bind the server to", Number.class,
			(value) -> ServerConfig.getInstance().setPort(value.intValue())));
		argumentParser.addArgument(new Argument<>("--no-entropy", "Disables the secure salt-generation and uses an unsafe one", Boolean.class,
			(value) -> ServerConfig.getInstance().setNoEntropy(value)));

		try {
			argumentParser.parse(args);
		} catch (final ArgumentParseException e) {
			System.out.println("Errors while parsing arguments, errors were:");
			System.out.println(e.getMessage());
			System.exit(1);
		}

		argumentParser.updateConfig();


		Server server = new Server();
		server.start();
	}

}

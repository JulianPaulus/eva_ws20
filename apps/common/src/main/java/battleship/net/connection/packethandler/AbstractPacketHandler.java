package battleship.net.connection.packethandler;

import battleship.net.connection.Connection;
import battleship.net.connection.GameConnection;
import battleship.net.exception.IllegalPacketTypeException;
import battleship.net.packet.IPacket;
import battleship.net.packet.IReceivePacket;

import java.util.Arrays;

public abstract class AbstractPacketHandler<ConnectionT extends Connection, PacketT extends IReceivePacket<ConnectionT>> {
	private final Class<ConnectionT> connectionTClass;
	private final Class<PacketT> packetClass;

	public AbstractPacketHandler(Class<ConnectionT> connectionTClass, Class<PacketT> packetClass) {
		this.packetClass = packetClass;
		this.connectionTClass = connectionTClass;
	}


	public void handle(IReceivePacket<?> packet, Connection connection) {
		if(!packetClass.isAssignableFrom(packet.getClass())) {
			throw new IllegalPacketTypeException("Packet doesn't match the Packethandler Packet-Type: Expected packet Type: " + packetClass.getName() + ". Receive Packet: " + packet.getClass().getName());
		}
		if(!connectionTClass.isAssignableFrom(connection.getClass())) {
			throw new IllegalPacketTypeException("Connection doesn't match the Packethandler Connection-Type");
		}
		handleImplementedPacketType(packetClass.cast(packet), connectionTClass.cast(connection));
		packetClass.cast(packet).act(connectionTClass.cast(connection));
	}

	protected abstract void handleImplementedPacketType(PacketT packet, ConnectionT connection);

}

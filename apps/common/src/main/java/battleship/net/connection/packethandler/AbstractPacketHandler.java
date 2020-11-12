package battleship.net.connection.packethandler;

import battleship.net.connection.Connection;
import battleship.net.connection.GameConnection;
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
			throw new IllegalArgumentException("Packet doesn't match the Packethandler Packet-Type");
		}
		if(!connectionTClass.isAssignableFrom(connection.getClass())) {
			throw new IllegalArgumentException("Connection doesn't match the Packethandler Connection-Type");
		}
		handleImplementedPacketType(packetClass.cast(packet), connectionTClass.cast(connection));
	}

	protected abstract void handleImplementedPacketType(PacketT packet, ConnectionT connection);

}

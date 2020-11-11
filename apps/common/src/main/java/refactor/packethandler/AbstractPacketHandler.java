package refactor.packethandler;

import battleship.net.packet.AbstractPacket;
import refactor.Connection;

public abstract class AbstractPacketHandler<PacketT extends AbstractPacket, ConnectionT extends Connection> {
	private final Class<PacketT> packetTClass;
	private final Class<ConnectionT> connectionTClass;

	public AbstractPacketHandler(Class<PacketT> packetTClass, Class<ConnectionT> connectionTClass) {
		this.packetTClass = packetTClass;
		this.connectionTClass = connectionTClass;
	}

	public void handle(AbstractPacket packet, Connection connection) {
		if(!packetTClass.isAssignableFrom(packet.getClass())) {
			throw new IllegalArgumentException("Packet doesn't match the Packethandler packet-type");
		}
		if(!connectionTClass.isAssignableFrom(packet.getClass())) {
			throw new IllegalArgumentException("Connection doesn't match the Packethandler connection-type");
		}
		handleImplementedPacketType(packetTClass.cast(packet), connectionTClass.cast(connection));
	}

	protected abstract void handleImplementedPacketType(PacketT packet, ConnectionT connection);

}

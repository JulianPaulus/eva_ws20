package battleships.net.connection.packethandler;

import battleships.net.connection.Connection;
import battleships.net.exception.IllegalPacketTypeException;
import battleships.net.packet.IReceivePacket;

public abstract class AbstractPacketHandler<ConnectionT extends Connection, PacketT extends IReceivePacket<ConnectionT>> {
	private final Class<ConnectionT> connectionTClass;
	private final Class<PacketT> packetClass;

	public AbstractPacketHandler(final Class<ConnectionT> connectionTClass, final Class<PacketT> packetClass) {
		this.packetClass = packetClass;
		this.connectionTClass = connectionTClass;
	}


	public void handle(final IReceivePacket<?> packet, final Connection connection) {
		if(!packetClass.isAssignableFrom(packet.getClass())) {
			throw new IllegalPacketTypeException("Packet doesn't match the Packethandler Packet-Type: Expected packet type: " + packetClass.getName() + ". Received packet: " + packet.getClass().getName());
		}
		if(!connectionTClass.isAssignableFrom(connection.getClass())) {
			throw new IllegalPacketTypeException("Connection doesn't match the Packethandler Connection-Type");
		}
		handleImplementedPacketType(packetClass.cast(packet), connectionTClass.cast(connection));
	}

	protected abstract void handleImplementedPacketType(final PacketT packet, final ConnectionT connection);

}

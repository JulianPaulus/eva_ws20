package battleships.server.packet;

import battleships.net.packet.IReceivePacket;
import battleships.server.connection.AuthenticatedConnection;

public interface ILobbyReceivePacket extends IReceivePacket<AuthenticatedConnection> {
}

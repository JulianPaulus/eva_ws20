package battleship.iface;

import battleship.packet.PacketLobby;

import java.util.Collection;

public interface ILobbyListController {

	void setLobbies(Collection<PacketLobby> lobbies);

}

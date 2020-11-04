package battleship.net.packet;

import java.io.IOException;

public abstract class AbstractPacket {

	public abstract byte[] marshal() throws IOException;

}

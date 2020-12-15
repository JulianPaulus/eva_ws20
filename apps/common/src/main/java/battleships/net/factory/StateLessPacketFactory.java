package battleships.net.factory;

import battleships.net.packet.IReceivePacket;

import java.io.DataInputStream;
import java.io.IOException;

public class StateLessPacketFactory<T extends IReceivePacket<?>> extends AbstractPacketFactory<T> {
	private Class<T> tClass;

	public StateLessPacketFactory(Class<T> tClass) {
		this.tClass = tClass;
	}

	@Override
	public T unmarshal(DataInputStream stream) throws IOException {
		try {
			return this.tClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
}

package battleship.util;

import battleship.net.packet.AbstractPacket;
import battleship.net.PacketType;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PacketReader extends Thread {

	private final BlockingQueue<AbstractPacket> queue;
	private final DataInputStream stream;

	public PacketReader(final InputStream stream) throws IOException {
		this.queue = new LinkedBlockingQueue<>();
		this.stream = new DataInputStream(stream);
	}

	@Override
	public void run() {
		while(!isInterrupted()) {
			try {
				byte identifier = stream.readByte();
				Optional<PacketType> optionalType = PacketType.getByIdentifier(identifier);
				optionalType.ifPresent(type -> {
					AbstractPacket packet = null;
					try {
						packet = type.getFactory().unmarshal(stream);
					} catch (IOException e) {
						e.printStackTrace();
					}
					queue.add(packet);
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public AbstractPacket readBlocking() throws InterruptedException {
		return queue.take();
	}

	public AbstractPacket read() throws InterruptedException {
		return queue.isEmpty() ? null : queue.take();
	}

}

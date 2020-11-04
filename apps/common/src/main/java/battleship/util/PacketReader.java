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

	public PacketReader(InputStream stream) throws IOException {
		this.queue = new LinkedBlockingQueue<>();
		this.stream = new DataInputStream(stream);
	}

	@Override
	public void run() {

		while(!isInterrupted()) {
			try {
				byte identifier = stream.readByte();
				Optional<PacketType> optionalType = PacketType.getByIdentifier(identifier);
				if (optionalType.isPresent()) {
					PacketType type = optionalType.get();
					AbstractPacket packet = type.getFactory().unmarshall(stream);
					queue.add(packet);
				} else {
					System.err.println("unknown identifier read");
				}
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

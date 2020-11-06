package battleship.util;

import battleship.net.packet.AbstractPacket;
import battleship.net.PacketType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PacketReader extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(PacketReader.class);

	private final BlockingQueue<AbstractPacket> queue;
	private final DataInputStream stream;
	private final Connection connection;

	public PacketReader(final InputStream stream, final Connection connection) {
		this.queue = new LinkedBlockingQueue<>();
		this.stream = new DataInputStream(stream);
		this.connection = connection;
	}

	@Override
	public void run() {
		while(!isInterrupted()) {
			try {
				byte identifier = stream.readByte();
				Optional<PacketType> optionalType = PacketType.getByIdentifier(identifier);
				if (optionalType.isPresent()) {
					PacketType type = optionalType.get();
					AbstractPacket packet = type.getFactory().unmarshal(stream);
					queue.add(packet);
				}
			} catch (IOException e) {
				try {
					connection.close();
					interrupt();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
		}
	}

	public AbstractPacket readBlocking() throws InterruptedException {
		return queue.take();
	}

	public AbstractPacket read() throws InterruptedException {
		return queue.isEmpty() ? null : queue.take();
	}

	public void close() throws IOException {
		this.interrupt();
		stream.close();
	}

}

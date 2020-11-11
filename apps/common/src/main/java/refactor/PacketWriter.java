package refactor;

import battleship.net.packet.AbstractPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PacketWriter extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(PacketWriter.class);

	private final BlockingQueue<AbstractPacket> queue;
	private final OutputStream stream;
	private final Connection connection;

	public PacketWriter(final OutputStream stream, final Connection connection) {
		this.queue = new LinkedBlockingQueue<>();
		this.stream = stream;
		this.connection = connection;
	}

	@Override
	public void run() {
		while(!isInterrupted()) {
			try {
				AbstractPacket packet = queue.take();
				byte[] data = packet.marshal();
				stream.write(data);
			} catch (IOException e) {
				//TODO do something useful?
				logger.error("PacketWriter could not write packet!");
				try {
					connection.close();
					interrupt();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			} catch (InterruptedException e) {
				//TODO do something useful?
				logger.error("PacketWriter has been interrupted!");
				interrupt();
				try {
					connection.close();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
		}

	}

	public void write(final AbstractPacket packet) {
		queue.add(packet);
	}

	public void close() throws IOException {
		this.interrupt();
		stream.close();
	}
}

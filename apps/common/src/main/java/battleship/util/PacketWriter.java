package battleship.util;

import battleship.net.packet.AbstractPacket;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PacketWriter extends Thread {

	private final BlockingQueue<AbstractPacket> queue;
	private final OutputStream stream;

	public PacketWriter(OutputStream stream) {
		this.queue = new LinkedBlockingQueue<>();
		this.stream = stream;
	}

	@Override
	public void run() {

		while(!isInterrupted()) {
			try {
				AbstractPacket packet = queue.take();
				byte[] data = packet.marshal();
				stream.write(data);
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void write(AbstractPacket packet) {
		queue.add(packet);
	}
}

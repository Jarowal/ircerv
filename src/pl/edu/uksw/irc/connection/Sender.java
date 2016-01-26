package pl.edu.uksw.irc.connection;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import pl.edu.uksw.irc.dto.MessageDTO;
import pl.edu.uksw.irc.queue.EventBus;

public class Sender implements Runnable {

	EventBus bus;
	int writeFrequency;

	public Sender(EventBus bus, int writeFrequency) {
		this.bus = bus;
		this.writeFrequency = writeFrequency;
	}

	@Override
	public void run() {
		while (true) {
			// System.out.println("SenderLoopStart");

			MessageDTO message = bus.getOutgoingEvent();

			if (message != null) {
				boolean success = false;
				try {
					// SocketChannel readChan = null;
					SocketChannel readChan = (SocketChannel) message.getTo()
							.channel();
					// String reply = "tst";
					if (message.getTo().isWritable()) {
						String reply = message.getUnparsedMessage() + "\r\n";
						ByteBuffer buf = ByteBuffer
								.allocate(reply.getBytes().length);
						buf.clear();
						buf.put(reply.getBytes());

						buf.flip();
						while (buf.hasRemaining()) {

							readChan.write(buf);
						}
						
					}
					success = true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (success != true) {
					bus.pushOutgoingEvent(message);
				}
			} else {
				try {
					Thread.sleep(writeFrequency);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// System.out.println("SenderLoopEnd");
		}

	}

}

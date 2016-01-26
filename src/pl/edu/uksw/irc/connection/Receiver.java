package pl.edu.uksw.irc.connection;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import pl.edu.uksw.irc.dto.MessageDTO;
import pl.edu.uksw.irc.queue.EventBus;

public class Receiver implements Runnable {

	Selector selector;
	EventBus bus;
	int readFrequency;

	public Receiver(Selector selector, EventBus bus, int readFrequency) {
		this.bus = bus;
		this.selector = selector;
		this.readFrequency = readFrequency;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			// System.out.println("tst");
			try {

				int howManyChannelsReady = selector.selectNow();
				// System.out.println("how many: " + howManyChannelsReady);
				if (howManyChannelsReady > 0) {

					Iterator<SelectionKey> keyIter = selector.selectedKeys()
							.iterator();
					// System.out.println("tst2");

					while (keyIter.hasNext()) {

						SelectionKey keyReady = keyIter.next();
						SocketChannel readChan = (SocketChannel) keyReady
								.channel();
						if (!keyReady.isValid()) {
							keyReady.cancel();
						}

						if (keyReady.isReadable()) {
							System.out.println("Receiving stuff!");

							ByteBuffer readBuf = ByteBuffer.allocate(2048);

							int fail = readChan.read(readBuf);
							readBuf.flip();
							String receivedMessage = "";
							System.out.println("fail: " + fail);
							if (fail == -1) {
								System.out.print("Closing connection");
								// System.out.print(receivedMessage);
								// keyReady.cancel();
								readChan.close();
								// keyIter.remove();
								break;
							}
							while (readBuf.hasRemaining()) {

								// read 1 byte at a time
								receivedMessage += (char) readBuf.get();

							}

							String[] splittedMessage = receivedMessage
									.split("\r\n|[\r\n]");
							for (String singleMessage : splittedMessage) {
								System.out.println("*"+singleMessage+"*");
								// System.out.println('*');
								// System.out.println("splitu");

								// MessageDTO message = null;
								MessageDTO message = new MessageDTO(
										singleMessage, keyReady);

								bus.pushIncomingEvent(message);

							}
						} else {
							howManyChannelsReady--;
						}
						keyIter.remove();
					}
				}
				if (howManyChannelsReady <= 0) {
					Thread.sleep(readFrequency);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println("tstEND");
		}
	}

}
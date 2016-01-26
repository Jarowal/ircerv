package pl.edu.uksw.irc.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Accepter implements Runnable {

	ServerSocketChannel serverSocketChannel;

	long time;
	Selector selector;

	public Accepter(int portNumber) {
		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket()
					.bind(new InetSocketAddress(portNumber));
			serverSocketChannel.configureBlocking(false);
			selector = Selector.open();
			time = System.currentTimeMillis();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Selector getSelector() {
		return selector;
	}

	public void run() {

		while (true) {

			SocketChannel socketChannel;
			try {
				socketChannel = serverSocketChannel.accept();
				if (socketChannel != null) {
					System.out.println("Connection Established!");

					socketChannel.configureBlocking(false);

					SelectionKey key = socketChannel.register(selector,
							SelectionKey.OP_READ | SelectionKey.OP_WRITE);
					
				} else {
					long time2 = System.currentTimeMillis();
					if (Math.abs(time - time2) > 1000) {
						time = time2;
						System.out.println("...");
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

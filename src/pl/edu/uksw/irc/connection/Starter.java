package pl.edu.uksw.irc.connection;

import java.nio.channels.Selector;

import pl.edu.uksw.irc.queue.EventBus;

public class Starter{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		EventBus bus = new EventBus();
		
		Accepter accepter = new Accepter(9999);
		Thread accepterThread = new Thread(accepter);
		accepterThread.start();
		System.out.println("Accepter started");
		
		Selector selector = accepter.getSelector();
						
		Receiver receiver = new Receiver(selector, bus, 1000);
		Thread receiverThread = new Thread(receiver);
		receiverThread.start();
		System.out.println("Receiver started");
		
		Sender sender = new Sender(bus, 1000);
		Thread senderThread = new Thread(sender);
		senderThread.start();
		System.out.println("Sender started");		
	}

}

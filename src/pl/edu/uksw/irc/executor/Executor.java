package pl.edu.uksw.irc.executor;

import pl.edu.uksw.irc.dto.MessageDTO;
import pl.edu.uksw.irc.queue.EventBus;

/**
 * Created by sok_pomaranczowy on 23.01.16.
 */
public class Executor implements Runnable {

	private EventBus eventBus;

	public Executor(EventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	

	@Override
	public void run() {

		while (true) {

			MessageDTO incomingEvent = eventBus.getIncomingEvent();
			if (incomingEvent != null) {
				MessageDTO outgoingEvent = null;
				incomingEvent = MessageParser.parseMessage(incomingEvent);

				switch (incomingEvent.getCommand()) {
				case JOIN:
					outgoingEvent = join(incomingEvent);
				case NICK:
					outgoingEvent = nick(incomingEvent);
				case PING:
					outgoingEvent = ping(incomingEvent);
				case PRIVMSG:
					outgoingEvent = privmsg(incomingEvent);
				case QUIT:
					outgoingEvent = quit(incomingEvent);
				default:
					break;
				}

				if (outgoingEvent != null) {
					eventBus.pushOutgoingEvent(outgoingEvent);
				}
			}
		}
	}

	private MessageDTO quit(MessageDTO incomingEvent) {
		return null;
	}

	private MessageDTO privmsg(MessageDTO incomingEvent) {
		return null;
	}

	private MessageDTO ping(MessageDTO incomingEvent) {
		return null;
	}

	private MessageDTO nick(MessageDTO incomingEvent) {
		return null;
	}

	private MessageDTO join(MessageDTO incomingEvent) {
		return null;
	}
}
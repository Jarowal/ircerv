package pl.edu.uksw.irc.executor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pl.edu.uksw.irc.dto.MessageDTO;
import pl.edu.uksw.irc.queue.EventBus;

public class Executor implements Runnable {
	private EventBus eventBus;

	private List<User> userList;
	private List<Channel> channelList;

	public Executor(EventBus eventBus) {
		this.eventBus = eventBus;
		userList = new LinkedList<>();
		channelList = new LinkedList<>();
	}

	public void sendResponse(MessageDTO response) {
		eventBus.pushOutgoingEvent(response);
		System.out.println("Sending: " + response.getUnparsedMessage());
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try{
			MessageDTO incomingEvent = eventBus.getIncomingEvent();
			if (incomingEvent != null) {
				MessageDTO outgoingEvent = null;
				incomingEvent = MessageParser.parseMessage(incomingEvent);
				if (incomingEvent.getCommand()!=null){
				System.out.println("Received Command: "
						+ incomingEvent.getCommand().name());
				}else{
					System.out.println("Unrecognised Command received");
					continue;
				}
				switch (incomingEvent.getCommand()) {
				case JOIN:
					join(incomingEvent);
					break;
				case NICK:
					nick(incomingEvent);
					break;
				case PING:
					ping(incomingEvent);
					break;
				case PRIVMSG:
					privmsg(incomingEvent);
					break;
				case QUIT:
					quit(incomingEvent);
					break;
				case USER:
					user(incomingEvent);
					break;
				case LIST:
					list(incomingEvent);
					break;
				default:				
					break;
				}
			}
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void list(MessageDTO incomingEvent) {
		String list="";
		for (Channel ch:channelList){
			MessageDTO response = new MessageDTO();
			response.setTo(incomingEvent.getFrom());
			response.setUnparsedMessage(ch.channelName+" " + ch.membersList.size() + " :"+ch.channelTopic);
			sendResponse(response);	
		}
		
		MessageDTO response = new MessageDTO();
		response.setTo(incomingEvent.getFrom());
		response.setUnparsedMessage(":End of LIST");
		sendResponse(response);
		
		
		
		
	}

	private void user(MessageDTO incomingEvent) {
		// TODO Auto-generated method stub

		for (User u : userList) {
			if (u.userKey.equals(incomingEvent.getFrom())) {
				MessageDTO response = new MessageDTO();
				response.setTo(incomingEvent.getFrom());
				response.setUnparsedMessage("001 " + u.nickName + " :welcome");
				sendResponse(response);
			}
		}

		// userExists=true;

	}

	private void quit(MessageDTO incomingEvent) {
		// TODO Auto-generated method stub

	}

	private void privmsg(MessageDTO incomingEvent) {
		String target = incomingEvent.getMiddleParams()[0];
		User userFrom=null;
		for (User u : userList) {
			if (u.userKey.equals(incomingEvent.getFrom())) {
				userFrom=u;
			}
		}
		
		if (target.startsWith("#")){
			System.out.println("Message sent to channel: "+target);
			for (Channel ch:channelList){
				if (ch.channelName.equals(target)){
					for (User u:ch.membersList){
						MessageDTO response = new MessageDTO();
						response.setTo(u.userKey);
						response.setUnparsedMessage(":" + userFrom.nickName + " PRIVMSG "+target+" :"+incomingEvent.getTrailingParams());
						sendResponse(response);
					}
						
				}
			}
		}else{
			for (User u:userList){
				if (u.nickName.equals(target)){
				MessageDTO response = new MessageDTO();
				response.setTo(u.userKey);
				response.setUnparsedMessage(":" + userFrom.nickName + " PRIVMSG "+target+" :"+incomingEvent.getTrailingParams());
				sendResponse(response);
				}
			}
			
		}

	}

	private void ping(MessageDTO incomingEvent) {
		MessageDTO response = new MessageDTO();
		response.setTo(incomingEvent.getFrom());
		response.setUnparsedMessage(incomingEvent.getUnparsedMessage()
				.replaceFirst("PING", "PONG"));
		sendResponse(response);

		System.out.println("Pong sent");

	}

	private void join(MessageDTO incomingEvent) {
		// TODO Auto-generated method stub
		boolean channelExists=false;
		String channel = incomingEvent.getMiddleParams()[0];
		User user=null;
		for (User u:userList){
			if (u.userKey.equals(incomingEvent.getFrom())){
				user=u;
				break;
			}
		}
		for (Channel ch: channelList){
			if (ch.channelName.equals(channel)){
				ch.membersList.add(user);
				channelExists=true;
				break;
			}			
		}
		if (channelExists==false){
			Channel newChan =new Channel(channel, "No Topic Set", user);
			channelList.add(newChan);
			System.out.println("Create Channel: " + newChan.channelName);
		}
		
		
	}

	private void nick(MessageDTO incomingEvent) {
		boolean userExists = false;
		String nick = incomingEvent.getMiddleParams()[0];
		for (User u : userList) {
			if (u.nickName.equals(nick)) {
				MessageDTO response = new MessageDTO();
				response.setTo(incomingEvent.getFrom());
				response.setUnparsedMessage("433 Unable To Create User: "
						+ nick);
				sendResponse(response);
				userExists = true;
				System.out.println("Unable To Create User: " + nick);
				break;
			}
		}
		if (userExists == false) {
			User user = new User(incomingEvent.getFrom(), nick, "", "");
			userList.add(user);
			System.out.println("Create User: " + nick);
		}

	}

}

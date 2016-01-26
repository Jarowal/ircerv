package pl.edu.uksw.irc.executor;

import pl.edu.uksw.irc.dto.MessageDTO;

/**
 * Created by sok_pomaranczowy on 08.01.16.
 */
public class MessageRenderer {
    
    
    public MessageDTO renderMessage(MessageDTO msg) {
    	String message = "";
    	
        if(!msg.getName().isEmpty()) {
            message = ":"+msg.getName()+" ";
           
            if(!msg.getUser().isEmpty()) {
                message.trim();
                message = message + "!" + msg.getUser();
                if(!msg.getHost().isEmpty()) {
                    message = message + "@" + msg.getHost()+" ";
                }
            }
        }
        message = message + msg.getCommand().name() + " ";
        if(msg.getMiddleParams().length > 1) {
            for(int i=0;i<msg.getMiddleParams().length;i++) {
                message = message + msg.getMiddleParams()[i];
                if(i < msg.getMiddleParams()[i].length()) {
                    message = message + ", ";
                }
            }
        }
        else
            message = message + msg.getMiddleParams()[0];
        if(!msg.getTrailingParams().isEmpty()) {
            message = message + " :"+msg.getTrailingParams();
        }
        msg.setUnparsedMessage(message.trim());
        return msg;
        }
}
//zamienia akcje na wiadomosc

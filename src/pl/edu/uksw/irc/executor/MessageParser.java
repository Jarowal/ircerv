package pl.edu.uksw.irc.executor;

import pl.edu.uksw.irc.dto.Command;
import pl.edu.uksw.irc.dto.MessageDTO;

/**
 * Created by sok_pomaranczowy on 08.01.16.
 */
public class MessageParser {

    public static MessageDTO parseMessage(MessageDTO msg) {

        String unparsedMessage = msg.getUnparsedMessage();

        int begin;
        int end;
        String[] messageSplit;
        String[] paramsSplit = null;

        //prefix
        if (unparsedMessage.startsWith(":")) {
            messageSplit = unparsedMessage.split(" ", 3);
            
            msg.setPrefix(messageSplit[0]);
            if (msg.getPrefix().contains("!")) {
                begin = msg.getPrefix().indexOf(":");
                end = msg.getPrefix().indexOf("!");
                msg.setName(msg.getPrefix().substring(begin + 1, end));
                if (msg.getPrefix().contains("@")) {
                    begin = msg.getPrefix().indexOf("!");
                    end = msg.getPrefix().indexOf("@");
                    msg.setUser(msg.getPrefix().substring(begin + 1, end));
                } else {
                    begin = msg.getPrefix().indexOf("!");
                    end = msg.getPrefix().length();
                    msg.setUser(msg.getPrefix().substring(begin + 1, end));
                }
            }
            if (msg.getPrefix().contains("@")) {
                if (!msg.getPrefix().contains("!")) {
                    begin = msg.getPrefix().indexOf(":");
                    end = msg.getPrefix().indexOf("@");
                    msg.setName(msg.getPrefix().substring(begin + 1, end));
                }
                begin = msg.getPrefix().indexOf("@");
                end = msg.getPrefix().length();
                msg.setHost(msg.getPrefix().substring(begin + 1, end));
            } else {
                begin = msg.getPrefix().indexOf(":");
                end = msg.getPrefix().length();
                msg.setName(msg.getPrefix().substring(begin + 1, end));
            }
            //command
            //msg.setCommand(messageSplit[1]);
            if (messageSplit[1].equals("QUIT")) {
                msg.setCommand(Command.QUIT);
            } else if (messageSplit[1].equals("JOIN")) {
                msg.setCommand(Command.JOIN);
            } else if (messageSplit[1].equals("NICK")) {
                msg.setCommand(Command.NICK);
            } else if (messageSplit[1].equals("PRIVMSG")) {
                msg.setCommand(Command.PRIVMSG);
            } else if (messageSplit[1].equals("PING")) {
                msg.setCommand(Command.PING);
            } else if (messageSplit[1].equalsIgnoreCase("USER")) {
                msg.setCommand(Command.USER);
            }else if (messageSplit[0].equalsIgnoreCase("LIST")) {
                msg.setCommand(Command.LIST);
            }
            //parametry
            for (int i = 2; i < messageSplit.length; i++) {

                msg.setParams(msg.getParams() + messageSplit[i]);
            }

        } else {
            messageSplit = unparsedMessage.split(" ", 2);
            if (messageSplit[0].equals("QUIT")) {
                msg.setCommand(Command.QUIT);
            } else if (messageSplit[0].equals("JOIN")) {
                msg.setCommand(Command.JOIN);
            } else if (messageSplit[0].equals("NICK")) {
                msg.setCommand(Command.NICK);
            } else if (messageSplit[0].equals("PRIVMSG")) {
                msg.setCommand(Command.PRIVMSG);
            } else if (messageSplit[0].equals("PING")) {
                msg.setCommand(Command.PING);
            } else if (messageSplit[0].equalsIgnoreCase("USER")) {
                msg.setCommand(Command.USER);
            }else if (messageSplit[0].equalsIgnoreCase("LIST")) {
                msg.setCommand(Command.LIST);
            }
            //todo else
            for (int i = 1; i < messageSplit.length; i++) {
                msg.setParams(msg.getParams() + messageSplit[i]);
            }
        }
        
        if (msg.getParams().contains(":")) {
            end = msg.getParams().indexOf(":");
            String p = msg.getParams().substring(0, end);
            if (p.contains(",")) {

                paramsSplit = p.split(",");
                msg.setMiddleParams(paramsSplit);
            } else if (p.trim().contains(" ")) {
                paramsSplit = p.split(" ");
                msg.setMiddleParams(paramsSplit);
            } else {
                paramsSplit = new String[1];
                paramsSplit[0] = msg.getParams().substring(0, end);
                msg.setMiddleParams(paramsSplit);
            }
            msg.setTrailingParams(msg.getParams().substring(end + 1, msg.getParams().length()));
        } else if (!msg.getParams().isEmpty()) {
            paramsSplit = new String[1];
            paramsSplit[0] = msg.getParams();
            msg.setMiddleParams(paramsSplit);

        }

        return msg;
    }

}

//Zamienia wiadomosc na akcje

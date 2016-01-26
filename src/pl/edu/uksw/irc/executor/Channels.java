/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.blic 
 */
package pl.edu.uksw.irc.executor;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Anna Lukaszek-Zadrozna
 */
public class Channels {
    List<Channel> channels;
    
    

    public Channels() {
    }
    /**
     * 
     * @param member
     * @param channelsToJoin Jezeeli przyszlo w DTO 0 (wypisz mnie ze wszystkich channeli), to tu przekaz null
     */
    public void join(User member, List<Channel> channelsToJoin){
        if(channelsToJoin == null){
            Iterator<User> iterator ;
            for(Channel ch:channels){
                for(iterator= ch.membersList.iterator(); iterator.hasNext();){
                    User u = iterator.next();       
                    if(u.userKey == member.userKey){
                        iterator.remove();                        
                    }
                }
            }
            
        }else{
            for(Channel ch: channelsToJoin){
                Iterator<Channel> iterator;
                boolean exists = false;
                for(iterator = channels.iterator();iterator.hasNext();){
                    Channel chIterator = iterator.next();
                    if(chIterator.channelName.equals(ch.channelName))
                        exists = true;
                
                }
                if(!exists)
                    channels.add(new Channel(ch.channelName,ch.channelTopic,member));
                else
                    for(Channel chFind: channels){
                        if(chFind.channelName.equals(ch.channelName))
                            chFind.membersList.add(member);
                    }
            }
        }
        
    }
    
}

package pl.edu.uksw.irc;

/**
 * Created by Admin on 2015-12-16.
 */
public interface MessageParser {
    public MessageDTO parse(byte[] bytes);
}

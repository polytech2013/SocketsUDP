
package RX302;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 *
 * @author Mario
 */
public abstract class Node {
    
    static final int LENGTH = 512;
    
    protected DatagramSocket socket;
    protected DatagramPacket packet;
    protected byte[] buffer;
    
    abstract void run();
    
    protected void close() {
        socket.close();
    }
    
    protected byte[] encodeString(String string) throws UnsupportedEncodingException {
        return string.getBytes("ascii");        
    }
    
    protected String decodeString(byte[] buffer) throws UnsupportedEncodingException {
        return new String(buffer, "ascii");
    }
    
}

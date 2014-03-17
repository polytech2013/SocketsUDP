
package RX302;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 *
 * @author Mario
 */
public abstract class Node {
    
    private DatagramSocket socket;
    private DatagramPacket packet;
    private byte[] buffer;
    
    abstract void run();
    abstract void init();
    
    public void close() {
        socket.close();
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }

    public DatagramPacket getPacket() {
        return packet;
    }

    public void setPacket(DatagramPacket packet) {
        this.packet = packet;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }
    
}

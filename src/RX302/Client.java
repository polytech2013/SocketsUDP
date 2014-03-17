
package RX302;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client class
 * @author Mario
 */
public class Client extends Node {
    
    public Client() {
        init();
    }
    
    @Override
    public void init() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {     
        try {
            // Set message to send
            buffer = this.encodeString("hello server rx302");
            // Get server address
            InetAddress address = InetAddress.getLocalHost();
            // Create datagram packet to send
            packet = new DatagramPacket(buffer, buffer.length, address, Server.PORT);
            // Send packet
            socket.send(packet);
            // Prepare buffer for receiving
            buffer = new byte[LENGTH];
            // Prepare datagram packet for response
            packet = new DatagramPacket(buffer, buffer.length);
            // Wait for server response
            socket.receive(packet);
            // Display server response
            display(packet);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void display(DatagramPacket server) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder("Server rx302 ready: ");
        // IP
        sb.append(packet.getAddress().toString());
        sb.append(" on ");
        // Port
        sb.append(packet.getPort());
        // Message
        sb.append("\nMessage: ");
        sb.append(decodeString(packet.getData()));
        System.out.println(sb.toString());
    }
    
    public static void main(String[] args) {
        Client client = new Client();
        
        client.run();
    }
    
}

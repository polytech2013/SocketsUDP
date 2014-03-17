
package RX302;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server class
 * @author Mario
 */
public class Server extends Node {
    
    static final int PORT = 1028;
    
    public Server() {
        init();
    }
    
    @Override
    public void init() {
        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        try {
            // Prepare buffer for receiving
            buffer = new byte[LENGTH];
            // Prepare datagram packet
            packet = new DatagramPacket(buffer, buffer.length);
            // Wait for client message
            socket.receive(packet);
            // Display new client
            display(packet);
            // Prepare response
            buffer = this.encodeString("Server rx302 ready");
            // Create datagram packet for response
            packet = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
            // Send response
            socket.send(packet);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void display(DatagramPacket client) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder("New client: ");
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
        Server server = new Server();
        // Run server
        server.run();
    }
    
}

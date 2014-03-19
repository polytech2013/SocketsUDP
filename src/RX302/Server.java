
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
    
    static final int PORT = 1025;
    
    public Server() {
        init();
    }
    
    private void init() {
        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        try {
            // Create client worker counter
            int id = 1;
            System.out.println("-- Ready to receive --");
            while (true) {
                // Prepare buffer for receiving
                buffer = new byte[LENGTH];
                // Prepare datagram packet
                packet = new DatagramPacket(buffer, buffer.length);
                // Wait for client message
                socket.receive(packet);
                // Create client worker
                DatagramSocket workerSocket = new DatagramSocket(PORT + id);
                ClientWorker cw = new ClientWorker(id, workerSocket, packet);
                id++;
                // Start client worker
                new Thread(cw).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public static void main(String[] args) {
        Server server = new Server();
        // Run server
        server.run();
    }
    
}

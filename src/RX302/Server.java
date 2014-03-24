
package RX302;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server class
 * @author Mario
 */
public class Server extends Node {
    
    static final int PORT = 1025;
    static final int LIMIT = 2;
    
    private PortUtility portUtility;
    
    public Server() {
        init();
    }
    
    private void init() {
        // Initialise socket
        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Initialise available ports
        portUtility = PortUtility.getInstance();
    }
    
    @Override
    public void run() {
        try {
            System.out.println("-- Ready to receive --");
            while (true) {
                // Prepare buffer for receiving
                buffer = new byte[LENGTH];
                // Prepare datagram packet
                packet = new DatagramPacket(buffer, buffer.length);
                // Wait for client message
                socket.receive(packet);
                
                // Try to find a new port for the client worker
                ClientWorker cw;
                int newPort = portUtility.findAvailable();                
                if (newPort == 0) {
                    // No ports available, use same socket
                    cw = new ClientWorker(socket, packet);
                }
                else {
                    // Create client worker with new port
                    cw = new ClientWorker(newPort, packet);
                }
                // Start client worker
                new Thread(cw).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public static void main(String[] args) {
        Server server = new Server();
        // Run server
        server.run();
    }
    
}

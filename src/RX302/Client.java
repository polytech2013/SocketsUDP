
package RX302;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client class
 * @author Mario
 */
public class Client extends Node {
    
    static String SERVER = null;
    
    public Client() {
        init();
    }
    
    private void init() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {  
        String message = "";
        try {
            // Get server address
            InetAddress address;
            if (SERVER != null) {
                address = InetAddress.getByName(SERVER);
            }
            else {
                address = InetAddress.getLocalHost();
            }
            // Set connection message to send
            buffer = this.encodeString("Hello server rx302");                            
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
            
            // Save received port
            int workerPort = packet.getPort();
            // Ready for communication
            while (!message.contains("stop")) {
                // Prepare scanner
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter your message:");
                // Get message
                message = scanner.nextLine();
                // Set message to send
                buffer = this.encodeString(message);                            
                // Create datagram packet to send
                packet = new DatagramPacket(buffer, buffer.length, address, workerPort);
                // Send packet
                socket.send(packet);
            }
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
        // Run client
        client.run();
    }
    
}

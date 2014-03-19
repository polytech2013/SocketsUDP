
package RX302;

import static RX302.Node.LENGTH;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mario
 */
public class ClientWorker extends Node implements Runnable {
    
    private int id;
    
    public ClientWorker(int id, DatagramSocket socket, DatagramPacket packet) {
        this.id = id;
        this.socket = socket;
        this.packet = packet;
    }

    @Override
    public void run() {
        try {
            // Display new client
            displayNewClient();
            // Prepare response
            buffer = this.encodeString("Server rx302 ready");
            // Create datagram packet for response
            packet = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
            // Send response
            socket.send(packet);
            
            // Ready for communication
            System.out.println("Worker " + id + ": Ready to receive");
            boolean listening = true;
            while (listening) {
                // Prepare buffer for receiving
                buffer = new byte[LENGTH];
                // Prepare datagram packet
                packet = new DatagramPacket(buffer, buffer.length);
                // Wait for client message
                socket.receive(packet);
                // Display client message
                displayMessage();
                // Stop communication if message contains "stop"
                if (decodeString(packet.getData()).contains("stop")) {
                    this.close();
                    listening = false;
                    System.out.println("Worker " + id + ": Good bye");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientWorker.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    private void displayNewClient() throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder("-- Server --\n");
        sb.append("New client: ");
        // IP
        sb.append(packet.getAddress().toString());
        sb.append(" on ");
        // Port
        sb.append(packet.getPort());
        // Message
        sb.append("\nMessage: ");
        sb.append(decodeString(packet.getData()));
        sb.append("\n----------");
        System.out.println(sb.toString());
    }
    
    private void displayMessage() throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder("Worker " + id + " received: ");
        // Message
        sb.append(decodeString(packet.getData()));
        System.out.println(sb.toString());
    }
    
}

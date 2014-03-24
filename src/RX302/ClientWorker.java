
package RX302;

import static RX302.Node.LENGTH;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mario
 */
public class ClientWorker extends Node implements Runnable {
    
    private int id;
    private boolean portAvailable;
    
    public ClientWorker(DatagramSocket socket, DatagramPacket packet) {
        this.socket = socket;
        this.packet = packet;
        portAvailable = false;
    }
    
    public ClientWorker(int port, DatagramPacket packet) throws SocketException {
        this(new DatagramSocket(port), packet);
        PortUtility.getInstance().use(port);
        portAvailable = true;
    }

    @Override
    public void run() {
        try {
            if (!portAvailable) {
                // Send error to client
                buffer = this.encodeString("No ports available, please try again later");
                // Create datagram packet for response
                packet = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
                // Send response
                socket.send(packet);
            }
            else {
                // Display new client
                displayNewClient();
                // Prepare response
                buffer = this.encodeString("Server rx302 ready");
                // Create datagram packet for response
                packet = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
                // Send response
                socket.send(packet);

                // Ready for communication
                System.out.println("Worker " + socket.getLocalPort() + ": Ready to receive");
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
                        System.out.println("Worker " + socket.getLocalPort() + ": Good bye");
                        // Free port
                        PortUtility.getInstance().free(socket.getLocalPort());
                        // Close socket
                        this.close();
                        // Close thread
                        listening = false;                        
                    }
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
        StringBuilder sb = new StringBuilder("Worker " + socket.getLocalPort() + " received: ");
        // Message
        sb.append(decodeString(packet.getData()));
        System.out.println(sb.toString());
    }
    
}

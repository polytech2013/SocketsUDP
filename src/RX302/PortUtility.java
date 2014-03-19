
package RX302;

import java.util.HashMap;
import java.util.Map;

/**
 * Port utility class
 * @author Mario
 */
public class PortUtility {
    
    private static PortUtility instance;
    
    private HashMap<Integer, Boolean> ports;
    
    private PortUtility() {
        ports = new HashMap<>(Server.LIMIT);
        for (int i = 1; i <= Server.LIMIT; i++) {
            ports.put(Server.PORT + i, Boolean.FALSE);
        }
    }
    
    public int findAvailable() {
        for (Map.Entry<Integer, Boolean> port : ports.entrySet()) {
            if (!port.getValue()) {
                return port.getKey();
            }
        }
        return 0;
    }
    
    public void free(int number) {
        ports.put(number, Boolean.FALSE);
    }
    
    public void use(int number) {
        ports.put(number, Boolean.TRUE);
    }
    
    public static PortUtility getInstance() {
        if (instance == null) {
            instance = new PortUtility();
        }
        return instance;
    }        
}

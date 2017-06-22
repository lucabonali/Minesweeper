package server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Luca
 */
public class MainServer {
    static Map<Integer,String> playersMap = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) {
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(1099);
            ServerRemote serverRemote = new ServerRemote();
            registry.bind("serverRMI", serverRemote);
        } catch (RemoteException | AlreadyBoundException e) {
            System.out.println("Error in creating Registry");
        }
    }
}

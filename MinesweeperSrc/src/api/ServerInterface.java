package api;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Luca
 */
public interface ServerInterface extends Remote {
    /**
     * metodo che esegue il login del giocatore
     * @param username
     * @param password
     * @return
     * @throws RemoteException
     */
    boolean login(String username, String password) throws RemoteException;


}

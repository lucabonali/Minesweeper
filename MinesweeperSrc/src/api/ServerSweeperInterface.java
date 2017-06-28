package api;

import client.clientConnection.ClientSweeper;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Luca
 */
public interface ServerSweeperInterface extends Remote {
    /**
     * metodo che esegue il login del giocatore
     * @param username
     * @param password
     * @return
     * @throws RemoteException
     */
    PlayerSweeperInterface login(String username, String password) throws RemoteException;




}

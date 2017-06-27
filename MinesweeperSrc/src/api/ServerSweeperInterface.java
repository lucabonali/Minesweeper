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
    boolean login(String username, String password) throws RemoteException;

    /**
     * metodo che permette di creare una partita, attendendo che un giocatore vi partecipi
     * @param gameMod
     * @return
     * @throws RemoteException
     */
    boolean createGame(GameMod gameMod, ClientSweeperInterface clientSweeperInterface) throws RemoteException;


    void sendInterference() throws RemoteException;


}

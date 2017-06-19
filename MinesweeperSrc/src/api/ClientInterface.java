package api;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Luca
 */
public interface ClientInterface extends Remote {

    /**
     * metodo che crea una partita, i base alla modalità che gli viene passata
     * @param gameMod
     * @return
     */
    boolean createGame(GameMod gameMod) throws RemoteException;



}

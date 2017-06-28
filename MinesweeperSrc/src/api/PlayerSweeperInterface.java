package api;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Andrea
 * @author Luca
 */
public interface PlayerSweeperInterface extends Remote {

    /**
     * metodo che permette di creare una partita, attendendo che un giocatore vi partecipi
     * @param gameMod
     * @return
     * @throws RemoteException
     */
    boolean createGame(GameMod gameMod, ClientSweeperInterface clientSweeperInterface) throws RemoteException;


    void sendInterference() throws RemoteException;
}

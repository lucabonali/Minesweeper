package api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.ResultSet;

/**
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

    void sendLose() throws RemoteException;

    void surrender() throws RemoteException;

    void saveGame(int time , GameMod gameMod) throws RemoteException;

    void getScores(GameMod gameMod) throws RemoteException;


}

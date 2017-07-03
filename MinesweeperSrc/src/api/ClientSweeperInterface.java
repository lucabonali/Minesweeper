package api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.util.List;

/**
 * Interfaccia che rappresenta il Client, e quindi ha tutti i metodi chiamabili dal Server, per la modifica dell' interfaccia Grafica e
 * per notifiche dei messaggi
 * @author Luca
 */
public interface ClientSweeperInterface extends Remote {

    void setGame() throws RemoteException;

    void gameStarted() throws RemoteException;

    void startTimer() throws RemoteException;

    void sendInterference() throws RemoteException;

    void getInterference(Interferences interference) throws RemoteException;

    void getLose() throws  RemoteException;

    void getSurrended() throws RemoteException;

    void getResultSet(List<String> names , List<Integer> times) throws RemoteException;

}

package api;

import java.rmi.Remote;
import java.rmi.RemoteException;

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

}

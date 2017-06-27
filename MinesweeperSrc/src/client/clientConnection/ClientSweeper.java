package client.clientConnection;

import api.ClientSweeperInterface;
import api.GameMod;
import api.ServerSweeperInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author Luca
 */
public class ClientSweeper implements ClientSweeperInterface {
    private static final String SERVER = "serverSweeper";
    private Registry registry;
    private ServerSweeperInterface serverSweeper;
    private static ClientSweeper instance;
    private String userName, password;



    //Metodi richiamati dal client verso il serverSweeper

    /**
     * costruttore della classe che si occupa di fare il Lookup del Registry contenente l' oggetto serverSweeper
     */
    public ClientSweeper(String userName, String password){
        try {
            registry = LocateRegistry.getRegistry(1099);
            serverSweeper = (ServerSweeperInterface) registry.lookup(SERVER);
        } catch (RemoteException | NotBoundException e) {
            System.out.println("Error Lookup Registry");
        }
    }

    /**
     * esegue il login ritornando vero se a buon fine, falso altrimenti
     * @param userName
     * @param password
     * @return
     */
    public boolean login(String userName, String password){
        try {
            return serverSweeper.login(userName,password);
        } catch (RemoteException e) {
            System.out.println("Login ERROR");
            return false;
        }
    }


    public boolean createGame(GameMod gameMod) throws RemoteException {
        serverSweeper.createGame(gameMod);
        return false;
    }

    @Override
    public void gameStarted() throws RemoteException {

    }

    @Override
    public void sendDisturb() throws RemoteException {

    }


    /**
     * ritorna un' istanza dell' oggetto di questa classe
     * @return
     */
    public ClientSweeper getInstance(){
        return instance;
    }

    /**
     * metodo che crea un istanza di ClientSweeper, statico poichè ogni giocatore può avere una sola istanza
     * @param username
     * @param password
     * @return
     */
    public static ClientSweeper createInstance(String username, String password) {
        instance = new ClientSweeper(username,password);
        return instance;
    }
}

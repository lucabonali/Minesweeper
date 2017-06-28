package client.clientConnection;

import api.ClientSweeperInterface;
import api.GameMod;
import api.PlayerSweeperInterface;
import api.ServerSweeperInterface;
import client.GUI.gameGui.GameInterface;
import client.GUI.gameGui.MultiplayerGui;
import server.PlayerSweeper;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Luca
 */
public class ClientSweeper extends UnicastRemoteObject implements ClientSweeperInterface {
    private static final String SERVER = "serverSweeper";
    private Registry registry;
    private ServerSweeperInterface serverSweeper;
    private PlayerSweeperInterface playerSweeper;
    private static ClientSweeper instance;
    private MultiplayerGui multiplayerGui;
    private String userName, password;



    //Metodi richiamati dal client verso il serverSweeper

    /**
     * costruttore della classe che si occupa di fare il Lookup del Registry contenente l' oggetto serverSweeper
     */
    public ClientSweeper(String userName, String password) throws RemoteException {
        super();

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
    public void login(String userName, String password){
        try {
            playerSweeper = (PlayerSweeperInterface) serverSweeper.login(userName,password);
            System.out.println("Ottengo Player Sweeper");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public boolean createGame(GameMod gameMod) throws RemoteException {
        System.out.println("Creo la partita");
        playerSweeper.createGame(gameMod,this);
        System.out.println("Partita creata");
        return false;
    }

    public void setMultiplayerGui(MultiplayerGui multiplayerGui){this.multiplayerGui = multiplayerGui;}

    @Override
    public void setGame() throws RemoteException {

    }

    @Override
    public void gameStarted() throws RemoteException {

    }

    /**
     * metodo che fa partire il timer della partita multigiocatore
     * @throws RemoteException
     */
    @Override
    public void startTimer() throws RemoteException {
       multiplayerGui.setStarted(true);
       multiplayerGui.startTimer();
    }

    @Override
    public void sendInterference() throws RemoteException {

    }


    /**
     * ritorna un' istanza dell' oggetto di questa classe
     * @return
     */
    public static ClientSweeper getInstance(){
        return instance;
    }

    /**
     * metodo che crea un istanza di ClientSweeper, statico poichè ogni giocatore può avere una sola istanza
     * @param username
     * @param password
     * @return
     */
    public static ClientSweeper createInstance(String username, String password) throws RemoteException {
        instance = new ClientSweeper(username,password);
        return instance;
    }

    public PlayerSweeperInterface getPlayerSweeper(){return playerSweeper;}
}

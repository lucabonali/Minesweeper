package client.clientConnection;

import api.*;
import client.GUI.gameGui.MultiplayerGui;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.util.List;

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
    private List<String> scoreNames;
    private List<Integer> scoreTimes;
    private boolean logged = false;

    //Metodi richiamati dal client verso il serverSweeper



    /**
     * costruttore della classe che si occupa di fare il Lookup del Registry contenente l' oggetto serverSweeper
     */
    public ClientSweeper(String userName, String password) throws RemoteException {
        super();
        this.userName = userName;
        this.password = password;
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
            playerSweeper = (PlayerSweeperInterface) serverSweeper.login(userName,password,this);
            logged = true;
            System.out.println("Ottengo Player Sweeper");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean createGame(GameMod gameMod) throws RemoteException {
        System.out.println("Creo la partita " + gameMod.toString());
        playerSweeper.createGame(gameMod,this);
        return false;
    }


    public boolean isLogged() {
        return logged;
    }

    public void setMultiplayerGui(MultiplayerGui multiplayerGui){this.multiplayerGui = multiplayerGui;}

    public void saveGame(int time, GameMod gameMod){
        try {
            System.out.println("Chiamo SaveGame di playerSweeper");
            playerSweeper.saveGame(time,gameMod);
        } catch (RemoteException e) {
            System.out.println("Error saving game");
        }
    }

    @Override
    public void setGame() throws RemoteException {

    }

    @Override
    public void gameStarted() throws RemoteException {

    }

    public void sendLose(){
        try {
            playerSweeper.sendLose();
        } catch (RemoteException e) {
            System.out.println("Error sending lose report");
        }
    }

    public void getScores(GameMod gameMod){
        try {
            playerSweeper.getScores(gameMod);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

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
        playerSweeper.sendInterference();
    }

    @Override
    public void getInterference(Interferences interference) throws RemoteException{
        multiplayerGui.showInterference(interference);
    }

    @Override
    public void getLose() throws RemoteException {
        System.out.println("Prima di showOther");
        multiplayerGui.showOtherLose();
        System.out.println("L'altro ha abbandonato");
    }

    @Override
    public void getSurrended() throws RemoteException {
        multiplayerGui.showOtherLose();
    }

    @Override
    public void getResultSet(List<String> names, List<Integer> times) throws RemoteException {
        this.scoreNames = names;
        this.scoreTimes = times;
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

    public void sendSurrender() {
        try {
            playerSweeper.surrender();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public List<String> getScoreNames() {
        return scoreNames;
    }

    public List<Integer> getScoreTimes() {
        return scoreTimes;
    }
}

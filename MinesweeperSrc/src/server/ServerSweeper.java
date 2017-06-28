package server;

import api.ClientSweeperInterface;
import api.GameMod;
import api.PlayerSweeperInterface;
import api.ServerSweeperInterface;
import client.clientConnection.ClientSweeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;


/**
 * Classe che modella la comunicazione da Client verso Server
 * @author Luca
 */
public class ServerSweeper extends UnicastRemoteObject implements ServerSweeperInterface {
    private ConnectionDB connectionDB;
    private static final String QUERY_LOGIN = "";
    static List<Game> gameList;


    protected ServerSweeper() throws RemoteException {
        //connectionDB = new ConnectionDB();
        gameList = new ArrayList<>();
    }


    /**
     * Effettua il login al giocatore una volta connesso, aggiungendolo alla mappa dei giocatori nel MainServer
     * @param username
     * @param password
     * @return
     * @throws RemoteException
     */
    @Override
    public synchronized PlayerSweeperInterface login(String username, String password) throws RemoteException {
        if(MainServer.playersMap.get(username) == null) {
            MainServer.playersMap.put(username, password);
            System.out.println("Name : " + username + " Password : " + password);
            return new PlayerSweeper(username,password);
        }
        else{
            MainServer.playersMap.put(username,password);
            return new PlayerSweeper(username,password);
        }

    }






}
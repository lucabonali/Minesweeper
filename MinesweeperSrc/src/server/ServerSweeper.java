package server;

import api.ClientSweeperInterface;
import api.GameMod;
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
    private List<Game> gameList;


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
    public boolean login(String username, String password) throws RemoteException {
        if(MainServer.playersMap.get(username) == null) {
            MainServer.playersMap.put(username, password);
            System.out.println("Name : " + username + " Password : " + password);
            return true;
        }
        else if(!MainServer.playersMap.get(username).equals(password))
                 return false;
        else{
            MainServer.playersMap.put(username,password);
            return true;
        }

    }

     /**
     * Chiamato dal client quando si ha una richiesta di partita, se è già presente una partita allora la faccio iniziare
     * aggiungendo il giocatore, se non ne è presente una disponibile allora la creo e attendo che un' altro player si
     * connetta
     * @param gameMod
     * @return
     * @throws RemoteException
     */
    @Override
    public boolean createGame(GameMod gameMod, ClientSweeperInterface clientSweeperInterface) throws RemoteException {
        Game game = getFreeGame();
        game.addPlayer(clientSweeperInterface);

        return false;
    }

    /**
     * metodo che ricerca una partita con un giocatore in attesa, e se non la trova allora ne comincia una nuova
     * @return
     */
    private Game getFreeGame() {
        for(Game g : gameList){
            if(!g.isStarted())
                return g;
        }
        Game game = new Game();
        gameList.add(game);
        return game;
    }

    @Override
    public void sendInterference() throws RemoteException {

    }


}
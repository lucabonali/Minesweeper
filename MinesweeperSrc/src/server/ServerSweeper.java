package server;

import api.ClientSweeperInterface;
import api.GameMod;
import api.ServerSweeperInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Classe che modella la comunicazione da Client verso Server
 * @author Luca
 */
public class ServerSweeper extends UnicastRemoteObject implements ServerSweeperInterface {
    private ConnectionDB connectionDB;
    private static final String QUERY_LOGIN = "";
    private ClientSweeperInterface clientSweeper;


    protected ServerSweeper() throws RemoteException {
        //connectionDB = new ConnectionDB();
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
    public boolean createGame(GameMod gameMod) throws RemoteException {



        return false;
    }

    /**
     * metodo che aggiunge al server l' interfaccia del client, per completare la comunicazione e per poter
     * chiamare da qui i metodi sul client
     * @param clientSweeperInterface
     */
    @Override
    public void addClientInterface(ClientSweeperInterface clientSweeperInterface){
        this.clientSweeper = clientSweeperInterface;
    }








}
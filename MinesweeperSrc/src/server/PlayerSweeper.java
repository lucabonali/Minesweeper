package server;

import api.ClientSweeperInterface;
import api.GameMod;
import api.Interferences;
import api.PlayerSweeperInterface;



import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Random;

/**
 * @author Andrea
 * @author Luca
 */
public class PlayerSweeper extends UnicastRemoteObject implements PlayerSweeperInterface {
    private String userName, password;
    private ConnectionDB connectionDB;
    private Game game;
    private HashMap<GameMod, String> gameModMap;
    private ClientSweeperInterface clientSweeper;
    private HashMap<Integer, Interferences> interferenceMap;

    public PlayerSweeper(String userName, String password,ClientSweeperInterface clientSweeper) throws RemoteException {
        super();
        connectionDB = new ConnectionDB();
        this.userName = userName;
        this.password = password;
        this.clientSweeper = clientSweeper;
        initializeInterferences();
        initializeGameModMap();
    }

    private void initializeGameModMap() {
        gameModMap = new HashMap<>();
        gameModMap.put(GameMod.EASY,"EASY");
        gameModMap.put(GameMod.MEDIUM, "MEDIUM");
        gameModMap.put(GameMod.HARD, "HARD");
    }

    private void initializeInterferences() {
        interferenceMap = new HashMap<>();
        interferenceMap.put(1,Interferences.BOMB);
        interferenceMap.put(2,Interferences.NEUTRAL);
        interferenceMap.put(3,Interferences.TIMER);
        interferenceMap.put(4,Interferences.CHARGEBAR);
        interferenceMap.put(5,Interferences.COVER);
    }


    /**
     * Chiamato dal client quando si ha una richiesta di partita, se è già presente una partita allora la faccio iniziare
     * aggiungendo il giocatore, se non ne è presente una disponibile allora la creo e attendo che un' altro player si
     * connetta
     *
     * @param gameMod
     * @return
     * @throws RemoteException
     */
    @Override
    public boolean createGame(GameMod gameMod, ClientSweeperInterface clientSweeperInterface) throws RemoteException {
        this.clientSweeper = clientSweeperInterface;
        game = getFreeGame(gameMod);
        game.addPlayer(clientSweeperInterface);
        if (game.isFull()) {
            game.startGame();
            System.out.println("Inizio partita");
            return true;
        } else {

            return false;
        }
    }

    /**
     * metodo che ricerca una partita con un giocatore in attesa, e se non la trova allora ne comincia una nuova
     *
     * @return
     */
    private Game getFreeGame(GameMod gameMod) {

        for (Game g : ServerSweeper.gameList) {
            if (!g.isFull())
                return g;
        }
        Game gameReturn = new Game(gameMod);
        ServerSweeper.gameList.add(gameReturn);
        return gameReturn;
    }



    @Override
    public void sendInterference() throws RemoteException {
        int interference = new Random().nextInt(5);
        Interferences interf = interferenceMap.get(interference);
        game.getOtherPlayer(clientSweeper).getInterference(interf);
    }

    @Override
    public void sendLose() throws RemoteException {
        game.getOtherPlayer(clientSweeper).getLose();
    }

    @Override
    public void surrender() throws RemoteException {
        game.getOtherPlayer(clientSweeper).getSurrended();
    }

    @Override
    public void saveGame(int time, GameMod gameMod) throws RemoteException {
        System.out.println("Chiamo il metodo insert con parametri: "+userName +" "+ time+" " + gameMod);
        connectionDB.insert(userName,time,gameModMap.get(gameMod));
    }

    @Override
    public void getScores(GameMod gameMod) throws RemoteException {
        ScoreResult scoreResult = new ScoreResult(connectionDB.executeQuery(gameMod));
        clientSweeper.getResultSet(scoreResult.getNamesList(),scoreResult.getTimeList());
    }



}
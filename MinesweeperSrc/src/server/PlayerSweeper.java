package server;

import api.ClientSweeperInterface;
import api.GameMod;
import api.PlayerSweeperInterface;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Andrea
 * @author Luca
 */
public class PlayerSweeper extends UnicastRemoteObject implements PlayerSweeperInterface {
    private String userName, password;


    public PlayerSweeper(String userName, String password) throws RemoteException {
        super();
        this.userName = userName;
        this.password = password;
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
        Game game = getFreeGame();
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
    private Game getFreeGame() {
        for (Game g : ServerSweeper.gameList) {
            if (!g.isFull())
                return g;
        }
        Game game = new Game();
        ServerSweeper.gameList.add(game);
        return game;
    }



    @Override
    public void sendInterference() throws RemoteException {

    }

}
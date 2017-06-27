package server;

import api.ClientSweeperInterface;

import java.util.List;

/**
 * Classe che modella il comportamento della partita, in due giocatori, i suoi metodi chiameranno e saranno chiamati dalla classe ServerSweeper
 * per la comunicazione con il client
 * @author Luca
 */
public class Game {
    private int gameMod;
    private boolean started;
    private List<ClientSweeperInterface> players;

    public Game(){

    }

    public void addPlayer(ClientSweeperInterface clientSweeperInterface){
        if(players.size()<2) {
            players.add(clientSweeperInterface);
            if(players.size() == 2)
                startGame();
        }
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isStarted() {
        return started;
    }

    /**
     * Metodo che inizia la partita
     */
    public void startGame() {
        setStarted(true);

    }


}

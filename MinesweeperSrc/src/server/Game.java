package server;

import api.ClientSweeperInterface;

/**
 * Classe che modella il comportamento della partita, in due giocatori, i suoi metodi chiameranno e saranno chiamati dalla classe ServerSweeper
 * per la comunicazione con il client
 * @author Luca
 */
public class Game {
    private int gameMod;
    private boolean started;
    private ClientSweeperInterface player1,player2;

    public Game(int gameMod, ClientSweeperInterface player1, ClientSweeperInterface player2){
        this.gameMod = gameMod;
        this.player1 = player1;
        this.player2 = player2;
        startGame();
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
    private void startGame() {
        setStarted(true);

    }


}

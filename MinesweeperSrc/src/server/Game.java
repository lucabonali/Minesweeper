package server;

import api.ClientSweeperInterface;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Classe che modella il comportamento della partita, in due giocatori, i suoi metodi chiameranno e saranno chiamati dalla classe ServerSweeper
 * per la comunicazione con il client
 * @author Luca
 */
public class Game {
    private int gameMod;
    private boolean started,isFull,ended;
    private List<ClientSweeperInterface> players;

    public Game(){
        players = new ArrayList<>();
    }

    public void addPlayer(ClientSweeperInterface clientSweeperInterface){
        if(players.size()<2) {
            players.add(clientSweeperInterface);
            if(players.size() == 2)
                isFull = true;
        }
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isStarted() {
        return started;
    }

    /**
     * Metodo che inizia la partita, settando la variabile isStarted a true e facendo partire il timer dei due
     * giocatori
     */
    public void startGame() {
        setStarted(true);
        Thread waitThread = new Thread(() -> {
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("Error Sleeping ");
            }
            players.forEach(clientSweeperInterface -> {
                try {
                    clientSweeperInterface.startTimer();
                    System.out.println("Faccio partire il timer di "+ clientSweeperInterface.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        });
        waitThread.start();
    }


    public boolean isFull() {
        return isFull;
    }


    public void endGame(){
        ended = true;
    }
}

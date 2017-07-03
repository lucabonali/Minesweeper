package server;

import api.ClientSweeperInterface;
import api.GameMod;
import api.PlayerSweeperInterface;

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
    private GameMod gameMod;
    private boolean started,isFull,ended;
    private List<ClientSweeperInterface> players;

    public Game(GameMod gameMod){
        this.gameMod = gameMod;
        players = new ArrayList<>();
    }

    public GameMod getGameMod() {
        return gameMod;
    }

    public void addPlayer(ClientSweeperInterface clientSweeperInterface){
        if(players.size()<2) {
            players.add(clientSweeperInterface);
            System.out.println("Aggiungo un giocatore alla partita: " + players.size());
            if(players.size() == 2)
                isFull = true;
        }
    }

    /**
     * metodo chiamato dalla classe PlayerSweeper che restituisce l' altro giocatore connesso alla partita
     * @param clientSweeperInterface
     * @return
     */
    public ClientSweeperInterface getOtherPlayer(ClientSweeperInterface clientSweeperInterface){
        for(ClientSweeperInterface clientSweeper : players){
            if(clientSweeper != clientSweeperInterface)
                return clientSweeper;
        }
        return null;
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

    public void playerSurrended(ClientSweeperInterface clientSweeperInterface){
        for(ClientSweeperInterface c : players){
            if(c == clientSweeperInterface){
                try {
                    c.getLose();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

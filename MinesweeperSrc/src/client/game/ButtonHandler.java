package client.game;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;

import java.util.ArrayList;
import java.util.List;


/**
 * Classe che modella il comportamento del clic che un bottone deve avere, solamente il modello funzionale, staccato dalla grafica
 * @author Luca
 */
public class ButtonHandler implements EventHandler<ActionEvent> {
    private int l, c, lMax, cMax, numberOfBombs, notBombButtons, countClicked=0;
    private Btn[][] buttons;
    private List<NearButtons> nearButtonsList;

    public ButtonHandler(int l, int c, Btn[][] buttons, int lMax, int cMax, int numberOfBombs){
        this.l = l;
        this.c = c;
        this.lMax = lMax;
        this.cMax = cMax;
        this.buttons = buttons;
        this.numberOfBombs = numberOfBombs;
        this.notBombButtons = lMax*cMax - numberOfBombs;
        initializeNearButtonList();
    }

    private void initializeNearButtonList() {
        nearButtonsList = new ArrayList<>();
        nearButtonsList.add(new NearButtons(l-1,c-1));
        nearButtonsList.add(new NearButtons(l-1,c));
        nearButtonsList.add(new NearButtons(l-1,c+1));
        nearButtonsList.add(new NearButtons(l,c-1));
        nearButtonsList.add(new NearButtons(l,c+1));
        nearButtonsList.add(new NearButtons(l+1,c-1));
        nearButtonsList.add(new NearButtons(l+1,c));
        nearButtonsList.add(new NearButtons(l+1,c+1));
    }

    /**
     * metodo iniziale del bottone, che controlla che non ci sia una bomba e che verifica se il pulsante non ha una bandiera
     * se è così lancia il metodo successivo, che si occupa di aprire la griglia
     * @param event
     */
    @Override
    public void handle(ActionEvent event) {
        if(buttons[l][c].isBomb() && !checkFlag(l,c)) {
            lose(l, c);
            return;
        }
        else if(!checkFlag(l,c)){
            clickButton(l,c);
            buttons[l][c].getGrid().getGameInterface().refreshScreen();
            if(hasWin(l,c)){
                buttons[l][c].getGrid().getGameInterface().win();
            }

        }
    }

    /**
     * metodo che clicca il bottone settando il suo parametro clicked a true, e controlla che le bombe vicine siano 0, se lo sono
     * apre anche tutti i bottoni vicini a lui
     * @param l linea
     * @param c colonna
     */
    private void clickButton(int l, int c) {
        buttons[l][c].setClicked(true);
        if(buttons[l][c].getNearBombs() == 0)
            openGrid(l,c);
    }

    /**
     * metodo richiamato quando viene premuto un pulsante che ha 0 bombe vicine, che apre ricorsivamente tutti i pulsanti
     * intorno a lui, chiamando
     * @param l
     * @param c
     */
    private void openGrid(int l , int c) {
        nearButtonsList.forEach(nearButton -> checkNullThenClick(nearButton.line,nearButton.column));
    }

    /**
     * metodo richiamato in openGrid, che controlla che la posizione passata esista
     * @param l
     * @param c
     */
    private void checkNullThenClick(int l, int c){
        try{
            if(!buttons[l][c].isClicked()) {
                buttons[l][c].fire();
            }
        }catch(ArrayIndexOutOfBoundsException | NullPointerException e){
            return;
        }
    }

    /**
     * controlla che il pulsante passatogli non sia flaggato
     * @param l
     * @param c
     * @return
     */
    private boolean checkFlag(int l , int c) {
            if (buttons[l][c].isFlaged())
                return true;
            return false;
    }

    /**
     * chiamato quando il giocatore tocca una bomba, la partita finisce
     */
    private void lose(int l , int c) {
        buttons[l][c].getGrid().getGameInterface().lose(l,c);
    }

    private boolean hasWin(int l, int c){
        countClicked = 0;
        for(int i = 0 ; i< lMax ; i++){
            for(int j = 0 ; j< cMax; j++){
                if(buttons[i][j].isClicked())
                    countClicked++;
            }
        }
        if(countClicked == notBombButtons)
            return true;
        else{
            countClicked = 0 ;
            return false;
        }
    }


    class NearButtons{
        int line, column;

        public NearButtons(int line, int column){
            this.line = line;
            this.column = column;
        }
    }


}

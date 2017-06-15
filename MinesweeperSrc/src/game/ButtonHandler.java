package game;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;


/**
 * Classe che modella il comportamento del clic che un bottone deve avere, solamente il modello funzionale, staccato dalla grafica
 * @author Luca
 */
public class ButtonHandler implements EventHandler<ActionEvent> {
    private int l, c, lMax, cMax;
    private Btn[][] buttons;

    public ButtonHandler(int l, int c, Btn[][] buttons, int lMax, int cMax){
        this.l = l;
        this.c = c;
        this.lMax = lMax;
        this.cMax = cMax;
        this.buttons = buttons;
    }

    /**
     * metodo iniziale del bottone, che controlla che non ci sia una bomba e che verifica se il pulsante non ha una bandiera
     * se è così lancia il metodo successivo, che si occupa di aprire la griglia
     * @param event
     */
    @Override
    public void handle(ActionEvent event) {
        if(buttons[l][c].isBomb() && !checkFlag(l,c))
            lose();
        else if(!checkFlag(l,c)){
            clickButton(l,c);
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
     * metodo che apre tutti i bottoni vicino al bottone che gli viene passato, se non nsono flaggati
     * @param l
     * @param c
     */
    private void openGrid(int l, int c) {
        if(!checkFlag(l-1,c-1)){
            openButton(l-1,c-1);
        }
        if(!checkFlag(l,c-1)){
            openButton(l,c-1);
        }
        if(!checkFlag(l+1,c-1)){
            openButton(l+1,c-1);
        }
        if(!checkFlag(l+1,c)){
            openButton(l+1,c);
        }
        if(!checkFlag(l+1,c+1)){
            openButton(l+1,c+1);
        }
        if(!checkFlag(l,c+1)){
            openButton(l,c+1);
        }
        if(!checkFlag(l-1,c+1)){
            openButton(l-1,c+1);
        }
        if(!checkFlag(l-1,c)){
            openButton(l-1,c);
        }
    }

    /**
     * Esegue la clickbutton su un bottone parametro, fa il catch quando il pulsante non esiste
     * @param l
     * @param c
     */
    private void openButton(int l, int c){
        try {
            clickButton(l, c);
        }catch(NullPointerException | ArrayIndexOutOfBoundsException e){
            return;
        }
    }

    /**
     * controlla che il pulsante passatogli non sia flaggato
     * @param l
     * @param c
     * @return
     */
    private boolean checkFlag(int l , int c){
        if(buttons[l][c].isFlaged())
            return true;
        return false;
    }

    /**
     * chiamato quando il giocatore tocca una bomba, la partita finisce
     */
    private void lose() {
        //Da implementare
    }


}

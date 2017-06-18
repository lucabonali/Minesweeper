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
            buttons[l][c].getGrid().getGameInterface().refreshScreen();

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
     * intorno a lui
     * @param l
     * @param c
     */
    private void openGrid(int l , int c) {
        checkNullThenClick(l-1,c-1);
        checkNullThenClick(l-1,c);
        checkNullThenClick(l-1,c+1);
        checkNullThenClick(l,c-1);
        checkNullThenClick(l,c+1);
        checkNullThenClick(l+1,c-1);
        checkNullThenClick(l+1,c);
        checkNullThenClick(l+1,c+1);
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
    private void lose() {

    }


}

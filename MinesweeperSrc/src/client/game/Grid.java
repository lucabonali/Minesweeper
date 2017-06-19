package client.game;

import client.GUI.gameGui.GameInterface;
import javafx.scene.input.MouseButton;

import java.util.Random;

/**
 * @author Luca
 */
public class Grid {
    private int btnCounter=0;
    private int lines, columns;
    private Btn[][] buttons;
    private int numberOfBombs;
    int currentBombs = 0;

    private GameInterface gameInterface;

    public Grid(int lines, int columns, GameInterface gameInterface){
        this.gameInterface = gameInterface;
        this.lines = lines;
        this.columns = columns;
        initializeButtons();
        initializeBombs(15); // il numero di bombe gli andrà passato dal costruttore insieme a linee e colonne
        initializeNearBombs();
    }

    public int getLines() {
        return lines;
    }

    public int getColumns() {
        return columns;
    }

    public Btn[][] getButtons() {
        return buttons;
    }

    public GameInterface getGameInterface() {
        return gameInterface;
    }

    /**
     * inizializa la griglia di bottoni, aggiungendo ad ognuno un proprio Handler
     */
    public void initializeButtons() {
        buttons = new Btn[lines][columns];
        for(int i = 0; i<lines;i++){
            for(int j = 0 ; j< columns; j++){
                buttons[i][j] = new Btn(btnCounter, this);
                buttons[i][j].setOnAction(new ButtonHandler(i,j, buttons, lines,columns));
                buttons[i][j].setOnMouseClicked(e -> {
                    Btn button = (Btn) e.getSource();
                    if(e.getButton().equals(MouseButton.SECONDARY)) {
                        System.out.println("Disarmo bottone");
                        button.disarm();
                    }
                });
                btnCounter++;
            }
        }
    }

    /**
     * inizializza la posizione delle bombe
     */
    private void initializeBombs(int numberOfBombs) {
        int lPos, cPos;

        lPos = new Random().nextInt(lines-1);
        cPos = new Random().nextInt(columns-1);

        if(!buttons[lPos][cPos].isBomb()) {
            buttons[lPos][cPos].setBomb(true);
            currentBombs++;
            System.out.println("Bottone  "+ lPos + cPos+buttons[lPos][cPos].isBomb() + " : " + currentBombs);
        }
        if(currentBombs < numberOfBombs)
            initializeBombs(numberOfBombs);
    }

    /**
     * inizializza il numero di bombe vicine al bottone
     */
    private void initializeNearBombs() {
        int nearBombs = 0;
        for(int i = 0; i<lines;i++){
            for(int j = 0; j< columns; j++){
                nearBombs = 0;
                if(getBombCounter(i-1,j-1))
                    nearBombs++;
                if(getBombCounter(i,j-1))
                    nearBombs++;
                if(getBombCounter(i+1,j-1))
                    nearBombs++;
                if(getBombCounter(i+1,j))
                    nearBombs++;
                if(getBombCounter(i+1,j+1))
                    nearBombs++;
                if (getBombCounter(i, j + 1))
                    nearBombs++;
                if (getBombCounter(i - 1, j + 1))
                    nearBombs++;
                if (getBombCounter(i - 1, j))
                    nearBombs++;
                buttons[i][j].setNearBombs(nearBombs);
            }
        }
    }
    /**
     * controlla che nel bottone passatogli come parametro ho una bomba e ritorna true in esito positivo
     * @param line
     * @param column
     * @return
     */
    private boolean getBombCounter(int line, int column){
        try {
            if (buttons[line][column].isBomb())
                return true;
            else
                return false;

        }catch (ArrayIndexOutOfBoundsException | NullPointerException e){
            return false;
        }
    }


}

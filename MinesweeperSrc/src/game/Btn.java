package game;

import javafx.scene.control.Button;

/**
 * @author Luca
 */
public class Btn extends Button {
    private int idButton;
    private int nearBombs = 0;
    private Grid grid;

    private boolean clicked,flaged,bomb;


    public int getIdButton() {
        return idButton;
    }

    public void setId(int id) {
        this.idButton = id;
    }

    public int getNearBombs() {
        return nearBombs;
    }

    public void setNearBombs(int nearBombs) {
        this.nearBombs = nearBombs;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public boolean isFlaged() {
        return flaged;
    }

    public void setFlaged(boolean flaged) {
        this.flaged = flaged;
    }

    public boolean isBomb() {
        return bomb;
    }

    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }

    public Grid getGrid() {
        return grid;
    }

    public Btn(int id, Grid grid){
        this.idButton = id;
        this.grid = grid;
        this.bomb = false;
        this.flaged = false;
        this.clicked = false;
    }


}

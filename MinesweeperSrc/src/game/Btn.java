package game;

import javafx.scene.control.Button;

/**
 * @author Luca
 */
public class Btn extends Button {
    private int idButton,nearBombs;

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

    public Btn(int id){
        this.idButton = id;

    }
}

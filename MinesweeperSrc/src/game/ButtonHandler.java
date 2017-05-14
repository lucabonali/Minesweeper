package game;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Created by Luca on 13/05/2017.
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

    @Override
    public void handle(ActionEvent event) {
        if(buttons[l][c].isBomb())
            lose();
        else
            checkFlag();
    }

    private void checkFlag() {
        if(!buttons[c][c].isFlaged())
            clickButton(l, c);
    }

    private void clickButton(int l , int c) {
        buttons[l][c].setClicked(true);
        if(buttons[l][c].getNearBombs() == 0)
            openGrid(l,c);
    }

    private void openGrid(int l, int c) {
        if(upperLeftCase(l,c))
            upperLeftOpen(l,c);
        if(bottomLeftCase(l,c))
            bottomLeftOpen(l,c);
        if(leftCase(l,c))
            leftCaseOpen(l,c);
        if(upperCase(l,c))
            upperCaseOpen(l,c);
        if(upperRightCase(l,c))
            upperRightOpen(l,c);
        if(rightCase(l,c))
            rightOpen(l,c);
        if(rightBottomCase(l,c))
            rightBottomOpen(l,c);
        if(bottomCase(l,c))
            bottomOpen(l,c);
        if(centerCase(l,c))
            centerOpen(l,c);
    }

    private void centerOpen(int l , int c) {
        clickButton(l-1,c-1);
        clickButton(l-1,c);
        clickButton(l-1,c+1);
        clickButton(l,c-1);
        clickButton(l,c+1);
        clickButton(l+1,c-1);
        clickButton(l+1,c);
        clickButton(l+1,c+1);
    }

    private boolean centerCase(int l , int c) {
        if(0<l && l<(lMax-1) && 0<c && c<(cMax-1))
            return true;
        return false;
    }

    private void bottomOpen(int l , int c) {
        clickButton(l,c-1);
        clickButton(l-1,c-1);
        clickButton(l-1,c);
        clickButton(l-1,c+1);
        clickButton(l,c+1);
    }

    private boolean bottomCase(int l , int c) {
        if(l==(lMax-1) && 0<c && c<(cMax-1))
            return true;
        return false;
    }

    private void rightBottomOpen(int l , int c) {
        clickButton(l-1,c);
        clickButton(l-1,c-1);
        clickButton(l,c-1);
    }

    private boolean rightBottomCase(int l , int c) {
        if(l==(lMax-1) && c==(cMax-1))
            return true;
        return false;
    }

    private void rightOpen(int l , int c) {
        clickButton(l-1,c);
        clickButton(l-1,c-1);
        clickButton(l,c-1);
        clickButton(l+1,c-1);
        clickButton(l+1,c);
    }

    private boolean rightCase(int l , int c) {
        if(c==(cMax-1) && 0<l && l<(lMax-1))
            return true;
        return false;
    }

    private void upperRightOpen(int l , int c) {
        clickButton(l,c-1);
        clickButton(l+1,c-1);
        clickButton(l+1,c);
    }

    private boolean upperRightCase(int l , int c) {
        if(l==0 && c ==(cMax-1))
            return true;
        return false;
    }

    private void upperCaseOpen(int l , int c) {
        clickButton(l,c-1);
        clickButton(l+1,c-1);
        clickButton(l+1,c);
        clickButton(l+1,c+1);
        clickButton(l,c+1);
    }

    private boolean upperCase(int l , int c) {
        if(l==0 && 0<c && c<(cMax-1))
            return true;
        return false;
    }

    private void leftCaseOpen(int l , int c) {
        clickButton(l-1,c);
        clickButton(l-1,c+1);
        clickButton(l,c+1);
        clickButton(l+1,c);
        clickButton(l+1,c+1);
    }

    private boolean leftCase(int l , int c) {
        if(c==0 && 0<l && l<(lMax-1))
            return true;
        return false;
    }

    private void bottomLeftOpen(int l , int c) {
        clickButton(l-1,c);
        clickButton(l-1,c+1);
        clickButton(l,c+1);
    }

    private boolean bottomLeftCase(int l , int c) {
        if(l==(lMax-1) && c==0)
            return true;
        return false;
    }

    private void upperLeftOpen(int l , int c) {
        clickButton( l ,c+1);
        clickButton(l+1,c);
        clickButton(l+1,c+1);
    }

    private boolean upperLeftCase(int l , int c) {
        if(l==0 && c==0)
            return true;
        return false;
    }

    private void lose() {
        //Da implementare
    }


}

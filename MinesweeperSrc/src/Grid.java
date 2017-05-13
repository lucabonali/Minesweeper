/**
 * Created by Luca on 13/05/2017.
 */
public class Grid {
    private int btnCounter=0;
    private int lines, columns;
    private Btn[][] buttons;

    public Grid(int lines, int columns){
        this.lines = lines;
        this.columns = columns;
        initializeButtons();
    }

    private void initializeButtons() {
        for(int i = 0; i<lines;i++){
            for(int j = 0 ; j< columns; j++){
                buttons[i][j] = new Btn(btnCounter);
                btnCounter++;
            }
        }
    }


}

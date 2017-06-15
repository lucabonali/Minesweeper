package game;

/**
 * @author Luca
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

    public int getLines() {
        return lines;
    }

    public int getColumns() {
        return columns;
    }

    public Btn[][] getButtons() {
        return buttons;
    }

    public void initializeButtons() {
        buttons = new Btn[lines][columns];
        for(int i = 0; i<lines;i++){
            for(int j = 0 ; j< columns; j++){
                buttons[i][j] = new Btn(btnCounter);
                buttons[i][j].setOnAction(new ButtonHandler(i,j, buttons, lines,columns));
                btnCounter++;
            }
        }
    }


}

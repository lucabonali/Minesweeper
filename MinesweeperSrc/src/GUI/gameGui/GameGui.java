package GUI.gameGui;

import GUI.MinesweeperLauncher;
import game.Btn;
import game.Grid;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.Pane;


import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.IOException;

/**
 * @author Andrea
 * @author Luca
 */
public class GameGui implements GameInterface {
    private Pane root;
    private double xOffset, yOffset;
    private Btn[][] buttons;
    private int lines,columns;

    public GameGui(int lines, int columns){
        this.root = new Pane();
        this.lines = lines;
        this.columns = columns;
        Grid grid = new Grid(lines,columns, this);
        this.buttons = grid.getButtons();
    }


    /**
     * metodo che crea il Pane principale della partita, e viene richiamato nel metodo CreateGameGui, per poi passarlo nella
     * funzione Lambda Platform.runLater() -> e farlo diventare la nuova Scene
     * @return
     */
    public Parent createContent(){
        addBackground();
        addButtons();
        return root;
    }

    /**
     * metodi di servizio che aggiungono l' immagine di sfondo, i bottoni, il titolo ecc
     * costruiscono la schermata della partita, e sono richiamati dal metodo createContent
     */
    @Override
    public void addButtons() {
        for(int i = 0; i< lines; i++){
            for(int j = 0; j<columns;j++){
                buttons[i][j].setPrefSize(25,20);
                buttons[i][j].setLayoutX(20+24*j);
                buttons[i][j].setLayoutY(20+25*i);
                addButtonAnimation(i,j);
                root.getChildren().add(buttons[i][j]);
            }
        }
    }

    /**
     * metodo che aggiunge l' animazione al bottone quando gli si passa sopra con il mouse
     * @param i
     * @param j
     */
    private void addButtonAnimation(int i , int j) {

    }

    private void addBackground() {
        ImageView bgImage = new ImageView(new Image(getClass().getResource("../resources/sfondoGame.png").toExternalForm()));
        bgImage.setFitWidth(400);
        bgImage.setFitHeight(500);
        bgImage.fitWidthProperty().bind(root.widthProperty());
        bgImage.fitHeightProperty().bind(root.heightProperty());
        bgImage.setOnMousePressed(event -> {
            xOffset = MinesweeperLauncher.getPrimaryStage().getX() -event.getScreenX();
            yOffset = MinesweeperLauncher.getPrimaryStage().getY() -event.getScreenY();
            bgImage.setCursor(Cursor.CLOSED_HAND);
        } );

        bgImage.setOnMouseDragged(event -> {
            MinesweeperLauncher.getPrimaryStage().setX(event.getScreenX() + xOffset);
            MinesweeperLauncher.getPrimaryStage().setY(event.getScreenY() + yOffset);
        });

        root.getChildren().add(bgImage);
    }

    public void setLinesAndColumns(int lines , int column){

    }



    public static void createGameGui(int lines, int columns) throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        GameGui nextView = new GameGui(lines,columns);
        Parent window = nextView.createContent();
        Platform.runLater(()->{
            Stage stage = MinesweeperLauncher.getPrimaryStage();
            Scene scene = new Scene(window, 400, 500);
            stage.setScene(scene);
            stage.centerOnScreen();
        });
    }
    @Override
    public void refreshScreen(){
        for(int i = 0 ; i < lines ; i++){
            for(int j = 0; j<columns;j++){
                if(buttons[i][j].isClicked()){
                    buttons[i][j].setOpacity(0.5);
                    if(buttons[i][j].getNearBombs() != 0) {
                        buttons[i][j].setText(String.valueOf(buttons[i][j].getNearBombs()));
                    }
                }
            }
        }
    }

}

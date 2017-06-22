package client.GUI.gameGui;

import client.game.Btn;
import client.GUI.MinesweeperLauncher;
import client.GUI.animations.ScaleAnimation;
import client.game.Grid;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.Pane;


import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.IOException;

/**
 * @author Andrea
 * @author Luca
 */
public class GameGui implements GameInterface {
    private int WIDTH, HEIGHT;


    private Pane root;
    private ToolBar toolBar;
    private double xOffset, yOffset;
    private Btn[][] buttons;
    private int lines,columns, numberOfBombs;

    public GameGui(int lines, int columns, int numberOfBombs){
        this.root = new Pane();
        this.lines = lines;
        this.columns = columns;
        this.numberOfBombs = numberOfBombs;
        this.WIDTH = columns * 25;
        this.HEIGHT = lines * 25;
        Grid grid = new Grid(lines,columns,numberOfBombs, this);
        this.buttons = grid.getButtons();
    }


    /**
     * metodo che crea il Pane principale della partita, e viene richiamato nel metodo CreateGameGui, per poi passarlo nella
     * funzione Lambda Platform.runLater() -> e farlo diventare la nuova Scene
     * @return
     */
    public Parent createContent(){
        addToolbar();
        addBackground();
        addButtons();
        return root;
    }

    /**
     * metodo che crea la toolbar e la aggiunge alla schermata di gioco
     */
    private void addToolbar() {
        toolBar = new ToolBar(new Button("ciao"));
        toolBar.setLayoutX(0);
        toolBar.setLayoutY(0);
        toolBar.setPrefSize(WIDTH,30);
        toolBar.setVisible(true);
        addToolbarButtons();
        root.getChildren().add(toolBar);
    }

    /**
     * metodo che aggiunge i bottoni della toolbar
     */
    private void addToolbarButtons() {
        Button home = new Button("gergweafs");
        home.setScaleX(20);
        home.setScaleY(20);
        home.setStyle(String.valueOf(getClass().getResourceAsStream("../styleSheet/graphicStyle.css")));
        home.setId("home");
        home.setOnAction(e -> backToLauncher());
        toolBar.getItems().add(home);
    }

    /**
     * metodo che ritorna al menu principale del gioco
     */
    private void backToLauncher() {
        Platform.runLater(()->{
            try {
                Parent window = FXMLLoader.load(getClass().getResource("fxml/launcher.fxml"));
                MinesweeperLauncher.getPrimaryStage().setScene(new Scene(window,500,275));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * metodo di servizio che crea l' immagine della bandiera e la ritorna
     */
    private ImageView createImageView(String path) {
        return new ImageView(new Image(getClass().getResourceAsStream("../resources/"+ path)));
    }

    /**
     * metodi di servizio che aggiungono l' immagine di sfondo, i bottoni, il titolo ecc
     * costruiscono la schermata della partita, e sono richiamati dal metodo createContent
     */
    @Override
    public void addButtons() {
        for(int i = 0; i< lines; i++){
            for(int j = 0; j<columns;j++){
                buttons[i][j].setPrefSize(25,25);
                buttons[i][j].setPadding(new Insets(0,0,0,0));
                buttons[i][j].setLayoutX(20+24*j);
                buttons[i][j].setLayoutY(50+25*i);
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


    /**
     * metodo che crea la finestra di gioco
     * @param lines
     * @param columns
     * @throws InterruptedException
     * @throws UnsupportedAudioFileException
     * @throws LineUnavailableException
     * @throws IOException
     */
    public static void createGameGui(int lines, int columns,int numberOfBombs) throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        GameGui nextView = new GameGui(lines,columns,numberOfBombs);
        Parent window = nextView.createContent();
        Platform.runLater(()->{
            Stage stage = MinesweeperLauncher.getPrimaryStage();
            Scene scene = new Scene(window, (columns*25+30), (lines*25+70) );
            stage.setScene(scene);
            stage.centerOnScreen();
        });
    }

    /**
     * Esegue il refresh dello schremo in maniera grafica, metodo di gameInterface.
     */
    @Override
    public void refreshScreen(){
        for(int i = 0 ; i < lines ; i++){
            for(int j = 0; j<columns;j++){
                refreshFlags(i,j);
                refreshClicked(i,j);
            }
        }
    }

    /**
     * prende come parametro una posizione e esegue il refresh
     * @param i
     * @param j
     */
    private void refreshClicked(int i , int j) {
        if(buttons[i][j].isClicked() && !buttons[i][j].isRefreshed()){
            buttons[i][j].setRefreshed(true);
            buttons[i][j].toFront();
            new ScaleAnimation(buttons[i][j],1.5,1.5, Duration.millis(400)).playAnimation(true);
            buttons[i][j].setOpacity(0.5);
            if(buttons[i][j].getNearBombs() != 0) {
                buttons[i][j].setText(String.valueOf(buttons[i][j].getNearBombs()));
            }
        }
    }

    /**
     * metodo che fa refresh dello stato delle bandiere, simile a refresh
     */
    private void refreshFlags(int i , int j) {
        if(buttons[i][j].isFlaged())
            buttons[i][j].setGraphic(createImageView("BandierinaTrasp.png"));
        else
            buttons[i][j].setGraphic(null);
    }


    /**
     * metodo che fa terminare la partita, chiamato dalla classe ButtonHandler su gameInterface, che mostra anche la posizione
     * delle bombe
     */
    @Override
    public void lose() {
        showBombs();
        try {
            createGameGui(lines,columns,numberOfBombs);
        } catch (InterruptedException | UnsupportedAudioFileException | LineUnavailableException | IOException e) {
           System.out.println("Error creating new Scene");
        }
    }

    /**
     * metodo che mostra le bombe una volta terminata la partita una perdita
     */
    private void showBombs() {
        for(int i = 0;i < lines ; i++){
            for(int j = 0; j < columns; j++){
                if(buttons[i][j].isBomb()) {
                    buttons[i][j].setGraphic(createImageView("MinaTrasp.png"));
                    buttons[i][j].toFront();
                    new ScaleAnimation(buttons[i][j], 2, 2, Duration.millis(1000)).playAnimation(true);
                }
            }
        }

    }



}

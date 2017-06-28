package client.GUI.gameGui;

import client.game.Btn;
import client.GUI.MinesweeperLauncher;
import client.GUI.animations.ScaleAnimation;
import client.game.Grid;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;


import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.IOException;

import static java.lang.Thread.sleep;

/**
 * @author Luca
 */
public class SinglePlayerGui implements GameInterface {
    private int WIDTH, HEIGHT;


    private Pane root;
    private ToolBar toolBar;
    private Label timerLabel;
    private double xOffset, yOffset;
    private Btn[][] buttons;
    private int lines,columns, numberOfBombs;
    private int buttonClicked = 0;

    public SinglePlayerGui(int lines, int columns, int numberOfBombs){
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
        addBackground(); // Deve essere eseguito prima di tutto
        addToolbar(WIDTH);
        addButtons();
        return root;
    }

    /**
     * metodo che crea la toolbar e la aggiunge alla schermata di gioco
     */
    public void addToolbar(int WIDTH) {
        toolBar = new ToolBar();
        toolBar.setLayoutX(0);
        toolBar.setLayoutY(0);
        toolBar.setPrefSize(WIDTH+30,30);
        toolBar.setVisible(true);
        addToolbarButtons();
        root.getChildren().add(toolBar);
    }

    /**
     * metodo che aggiunge i bottoni della toolbar
     */
    private void addToolbarButtons() {
        toolBar.getItems().addAll(homeButton(),addDivisorPane(),addTimer());
    }

    private Pane addDivisorPane() {
        Pane divisor = new Pane();
        divisor.setPrefWidth(200);
        divisor.setPrefHeight(30);
        return divisor;
    }

    private Label addTimer() {
        timerLabel = new Label();
        timerLabel.setFont(Font.font("AR JULIAN", 13));
        timerLabel.setEffect(new DropShadow());
        startTimer();
        return timerLabel;
    }

    public void startTimer() {
        Thread timerThread = new Thread(new Timer());
        timerThread.start();
    }

    private Button homeButton() {
        Button homeButton = new Button();
        homeButton.setPadding(new Insets(0,0,0,0));
        homeButton.setGraphic(createImageView("../resources/Home.png"));
        homeButton.setBackground(Background.EMPTY);
        homeButton.setCursor(Cursor.HAND);
        homeButton.setOnAction(e -> backToLauncher());
        homeButton.setOnMouseEntered(e -> new ScaleAnimation(homeButton,1.5,1.5, Duration.millis(500)).playAnimation(false));
        homeButton.setOnMouseExited(e -> new ScaleAnimation(homeButton,1,1, Duration.millis(500)).playAnimation(false));
        homeButton.setPrefSize(25,25);
        return homeButton;
    }

    /**
     * metodo che ritorna al menu principale del gioco
     */
    private void backToLauncher() {
        Platform.runLater(()->{
            try {
                Parent window = FXMLLoader.load(getClass().getResource("../fxml/launcher.fxml"));
                MinesweeperLauncher.getPrimaryStage().setScene(new Scene(window,500,275));
                MinesweeperLauncher.getPrimaryStage().centerOnScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * metodo di servizio che crea l' immagine e la ritorna ( in base al percorso cambia l' immagine che gli si ritorna
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
        SinglePlayerGui nextView = new SinglePlayerGui(lines,columns,numberOfBombs);
        Parent window = nextView.createContent();
        Platform.runLater(()->{
            Stage stage = MinesweeperLauncher.getPrimaryStage();
            Scene scene = new Scene(window, (columns*25+30), (lines*25+70) );
            stage.setScene(scene);
            stage.centerOnScreen();
        });
    }

    public Parent getRoot(){
        return root;
    }
    /**
     * Esegue il refresh dello schremo in maniera grafica, metodo di gameInterface.
     */
    @Override
    public void refreshScreen(){
        buttonClicked = 0;
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
            buttonClicked++;
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
    public void showBombs() {
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

    public void setHEIGHT(int HEIGHT) {
        this.HEIGHT = HEIGHT;
    }


    public int getHEIGHT(){
        return HEIGHT;
    }

    public void setWIDTH(int WIDTH){
        this.WIDTH = WIDTH;
    }

    public int getWIDTH(){
        return WIDTH;
    }

    public int getButtonClicked() {
        return buttonClicked;
    }
    public void setButtonClicked(int buttonClicked){ this.buttonClicked = buttonClicked;}

    public int getLines(){return lines;}
    public int getColumns(){return columns;}
    public int getNumberOfBombs(){return numberOfBombs;}






    class Timer implements Runnable {
        private int time = 0;
        private SinglePlayerGui singlePlayerGui;

        @Override
        public void run() {

            while (true){
                time++;
                setTime();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("ERROR SLEEPING TIMER");
                }
            }
        }

        public void setTime(){
            Platform.runLater(() -> {
                if(time < 10)
                    timerLabel.setText("00" + time);
                if(time > 10 && time< 100)
                    timerLabel.setText("0" + time);
                else
                    timerLabel.setText(""+time);
            });
        }

    }

}

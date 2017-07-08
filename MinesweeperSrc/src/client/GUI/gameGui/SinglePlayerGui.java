package client.GUI.gameGui;

import api.GameMod;
import client.GUI.animations.FadeAnimation;
import client.clientConnection.ClientSweeper;
import client.game.Btn;
import client.GUI.MinesweeperLauncher;
import client.GUI.animations.ScaleAnimation;
import client.game.GameInterface;
import client.game.Grid;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;


import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

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
    private Grid grid;
    private Thread timerThread;
    private Btn[][] buttons;
    private Button homeButton;
    private HashMap<Integer,Color> buttonColorMap;
    private int lines,columns, numberOfBombs;
    private GameMod gameMod;

    private int buttonClicked = 0;

    public SinglePlayerGui(int lines, int columns, int numberOfBombs, GameMod gameMod){
        this.root = new Pane();
        this.lines = lines;
        this.columns = columns;
        this.numberOfBombs = numberOfBombs;
        this.WIDTH = columns * 25;
        this.HEIGHT = lines * 25;
        this.grid = new Grid(lines,columns,numberOfBombs, this);
        this.buttons = grid.getButtons();
        this.gameMod = gameMod;
        initializeButtonColorMap();
    }
    private void initializeButtonColorMap() {
        buttonColorMap = new HashMap<>();
        buttonColorMap.put(1,Color.RED);
        buttonColorMap.put(2,Color.GREEN);
        buttonColorMap.put(3,Color.BLUE);
        buttonColorMap.put(4,Color.BLACK);
        buttonColorMap.put(5,Color.VIOLET);
        buttonColorMap.put(6,Color.YELLOW);
        buttonColorMap.put(7,Color.PINK);
        buttonColorMap.put(8,Color.TOMATO);
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
    public void addToolbarButtons() {
        toolBar.getItems().addAll(homeButton(),addDivisorPane(),restartButton(),addDivisorPane(),addTimer());
    }


    private Button restartButton() {
    Button restartButton = new Button();
    restartButton.setPadding(new Insets(0,0,0,0));
    restartButton.setGraphic(createImageView("../resources/restart.png"));
    restartButton.setBackground(Background.EMPTY);
    restartButton.setCursor(Cursor.HAND);
    restartButton.setOnAction(e -> restart());
    restartButton.setOnMouseEntered(e -> new ScaleAnimation(restartButton,1.5,1.5, Duration.millis(500)).playAnimation(false));
    restartButton.setOnMouseExited(e -> new ScaleAnimation(restartButton,1,1, Duration.millis(500)).playAnimation(false));
    restartButton.setPrefSize(25,25);
    return  restartButton;
    }

    private void restart() {
        try {
            createGameGui(lines,columns,numberOfBombs,gameMod);
        } catch (InterruptedException | UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("ERROR RESTARTING");
        }
    }

    public Pane addDivisorPane() {
        Pane divisor = new Pane();
        divisor.setPrefWidth(WIDTH/3);
        divisor.setPrefHeight(30);
        return divisor;
    }

    public Label addTimer() {
        timerLabel = new Label();
        timerLabel.setFont(Font.font("AR JULIAN", 13));
        timerLabel.setEffect(new DropShadow());
        startTimer();
        return timerLabel;
    }

    public void startTimer() {
        timerThread = new Thread(new Timer());
        timerThread.start();
    }

    public Button homeButton() {
        homeButton = new Button();
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

    public Button getHomeButton() {
        return homeButton;
    }

    /**
     * metodo che ritorna al menu principale del gioco
     */
    public void backToLauncher() {
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
    public static void createGameGui(int lines, int columns,int numberOfBombs,GameMod gameMod) throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        SinglePlayerGui nextView = new SinglePlayerGui(lines,columns,numberOfBombs,gameMod);
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
                buttons[i][j].setFont(new Font("AR JULIAN", 13));
                setButtonColor(i,j);
                refreshFlags(i,j);
                refreshClicked(i,j);
            }
        }
    }

    private void setButtonColor(int i , int j) {
               buttons[i][j].setTextFill(buttonColorMap.get(buttons[i][j].getNearBombs()));
    }

    /**
     * prende come parametro una posizione e esegue il refresh
     * @param i
     * @param j
     */
    private void refreshClicked(int i , int j) {
        if(buttons[i][j].isClicked() && !buttons[i][j].isRefreshed()){
            buttons[i][j].setRefreshed(true);
            buttonClicked ++;
            System.out.println("Bottoni cliccati =  " + buttonClicked);
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
    public void lose(int i , int j) {
        showBombs();
        disableButtons();
    }

    private void disableButtons() {
        for(int i = 0 ; i<lines; i++){
            for(int j = 0; j < columns ; j++){
                buttons[i][j].setDisable(true);
            }
        }
        timerThread.interrupt();
    }

    /**
     * metodo che termina la partita e mostra la schermata di vittoria
     */
    @Override
    public void win() {
        startRemoveAnimation();
        startWinAnimation();
        addEndButtons();
        timerThread.interrupt();
    }

    private void addEndButtons() {
        Button saveGame = new Button();
        Button restart = new Button();
        saveGame.setGraphic(createImageView("saveGame.png"));
        restart.setGraphic(createImageView("restartGame.png"));
        saveGame.setPadding(new Insets(0,0,0,0));
        saveGame.setBackground(Background.EMPTY);
        saveGame.setCursor(Cursor.HAND);
        restart.setPadding(new Insets(0,0,0,0));
        restart.setBackground(Background.EMPTY);
        restart.setCursor(Cursor.HAND);
        saveGame.setLayoutX(getWIDTH()/3 - getWIDTH()/4);
        saveGame.setLayoutY(getHEIGHT()-getHEIGHT()/7);
        restart.setLayoutX(getWIDTH()*2/3- getWIDTH()/11);
        restart.setLayoutY(getHEIGHT()-getHEIGHT()/7);

        saveGame.setOnAction(e -> saveGame());
        restart.setOnAction(e -> restart());
        root.getChildren().addAll(saveGame,restart);
    }

    private void saveGame() {
        if(ClientSweeper.getInstance() != null) {
            if (ClientSweeper.getInstance().isLogged()) {
                ClientSweeper.getInstance().saveGame(Integer.parseInt(timerLabel.getText()), gameMod);
            }
        }
        else{
            Stage login = new Stage();
            Platform.runLater(() -> {
                Parent window = null;
                try {
                    window = FXMLLoader.load(getClass().getResource("../fxml/login.fxml"));
                } catch (IOException e) {
                }
                login.setScene(new Scene(window, 350, 197));
                login.centerOnScreen();
                login.setOnCloseRequest(e -> System.exit(0));
                login.initStyle(StageStyle.UNDECORATED);
                login.show();
            });
        }

    }

    private void startWinAnimation() {
        Label winLabel = new Label("YOU WIN");
        winLabel.setFont(new Font("AR JULIAN", 25));
        winLabel.setTextFill(Color.WHITESMOKE);
        winLabel.setLayoutX(getWIDTH()-getWIDTH()/2 - (getWIDTH()/5));
        winLabel.setLayoutY(getHEIGHT()-getHEIGHT()/2);
        ScaleAnimation big = new ScaleAnimation(winLabel,2,2,Duration.millis(1000));
        ScaleAnimation small = new ScaleAnimation(winLabel,1,1,Duration.millis(1000));
        big.getScaleTransition().setOnFinished(e -> small.playAnimation(false));
        small.getScaleTransition().setOnFinished(e -> big.playAnimation(false));
        big.playAnimation(false);
        root.getChildren().add(winLabel);
    }

    /**
     * animazione che lancia la vittoria della partita
     */
    public void startRemoveAnimation() {
        for(int i = 0 ; i< lines ; i++){
            for(int j = 0 ; j <columns ; j++){
                new ScaleAnimation(buttons[i][j], 2,2,Duration.millis(500)).playAnimation(true);
                new FadeAnimation(buttons[i][j], 1,0,Duration.millis(1000)).playAnimation();
            }
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

    // METODI SET E GET CHE SERVONO ALLA CLASSE MULTIPLAYER GUI PER POTER OPERARE
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
    public Btn[][] getBtn() {return buttons;}

    public ToolBar getToolBar(){return toolBar;}
    public Label getTimerLabel() {
        return timerLabel;
    }

    public GameMod getGameMod() {
        return gameMod;
    }


    // CLASSE CHE IMPLEMENTA IL TIMER DI GIOCO; VIENE LANCIATO QUANDO INIZIA LA PARTITA , METOODO SETTIME CHE AGGIORNA LO STATO
    // DELL' ETICHETTA TIMERLABEL

    class Timer implements Runnable,Initializable {
        private int time = 0;
        private boolean stopped;

        @Override
        public void run() {
            Platform.runLater(() -> {
                timerLabel.setText(String.valueOf(time));
            });
            while (true){
                if(!stopped){
                    time++;
                    setTime();
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        stop();
                    }

                }
                else
                    break;
            }
        }

        private void stop() {
            stopped = true;
        }

        public void setTime(){
            if(time == 1 )
                Platform.runLater(() -> {
                    timerLabel.setText(String.valueOf(Integer.parseInt(timerLabel.getText())));
                });
            else {
                Platform.runLater(() -> {
                    timerLabel.setText(String.valueOf(Integer.parseInt(timerLabel.getText()) + 1));
                });
            }
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            Platform.runLater(() -> {
                timerLabel.setText(String.valueOf(time));
            });
        }
    }

}

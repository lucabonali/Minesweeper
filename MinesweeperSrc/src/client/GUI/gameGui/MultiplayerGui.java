package client.GUI.gameGui;

import api.GameMod;
import api.Interferences;
import client.GUI.MinesweeperLauncher;
import client.GUI.animations.FadeAnimation;
import client.GUI.animations.RotateAnimation;
import client.GUI.animations.ScaleAnimation;
import client.GUI.animations.TranslateAnimation;
import client.clientConnection.ClientSweeper;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.lang.management.PlatformLoggingMXBean;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Random;


/**
 * * @author Luca
 */
public class MultiplayerGui extends SinglePlayerGui {
    private boolean isStarted;
    private Pane barPane;
    private Pane chargeBar;
    private VBox barPaneVBox,chargePaneVBox;
    private ImageView mine,bar;
    private Pane multiRoot;
    private int barCharges = 0;
    private int lives = 3;
    private boolean livesAdded = false;
    private ImageView[] livesImage;
    private ImageView rocket;
    private double xPos, yPos;
    private ClientSweeper clientSweeper;
    private RotateAnimation start,right,left,end;
    private HashMap<Interferences,InterferenceHandler> interferenceMap;

    public MultiplayerGui(int lines, int columns, int numberOfBombs, GameMod gameMod){
        super(lines,columns,numberOfBombs,gameMod);
        setWIDTH(columns*25+100);
        setHEIGHT(lines*25+70);
        clientSweeper = ClientSweeper.getInstance();
        clientSweeper.setMultiplayerGui(this);
        initializeInterferenceMap();
    }

    private void initializeInterferenceMap() {
        interferenceMap = new HashMap<>();
        interferenceMap.put(Interferences.BOMB,this::bombInterference);
        interferenceMap.put(Interferences.NEUTRAL,this::neutralInterference);
        interferenceMap.put(Interferences.TIMER,this::timerInterference);
        interferenceMap.put(Interferences.CHARGEBAR,this::chargeBarInterference);
        interferenceMap.put(Interferences.COVER,this::coverInterference);
    }

    public void setStarted(boolean started){
        this.isStarted = started;
    }

    @Override
    public Parent createContent() {
        multiRoot = (Pane) super.createContent();
        addChargeBarPane();
        addLives();
        return multiRoot ;
    }

    @Override
    public void addToolbarButtons(){
        getToolBar().getItems().add(homeButton());
        addLives();
        getToolBar().getItems().addAll(this.addDivisorPane(),super.addTimer());
    }

    @Override
    public Button homeButton(){
        super.homeButton();
        getHomeButton().setOnMouseClicked(e -> clientSweeper.sendSurrender());
        return getHomeButton();
    }

    @Override
    public Pane addDivisorPane(){
        Pane divisor = new Pane();
        divisor.setPrefWidth(150);
        divisor.setPrefHeight(30);
        return divisor;
    }

    /**
     * metodo che aggiunge le vite sulla Toolbar
     */
    private void addLives() {
        if(!livesAdded) {
            livesImage = new ImageView[lives];
            for (int i = 0; i < lives; i++) {
                livesImage[i] = new ImageView(new Image(getClass().getResourceAsStream("../resources/MinaTrasp.png")));
                System.out.println("Aggiongo la mina :" + i);
                super.getToolBar().getItems().add(livesImage[i]);
            }
            livesAdded = true;
        }
    }


    @Override
    public void startTimer(){
        if(isStarted)
            super.startTimer();
    }

    private void addChargeBarPane() {
        barPane = new Pane();
        chargeBar = new Pane();
        barPane.setLayoutX(getWIDTH()-80);
        barPane.setLayoutY(getHEIGHT()-260);
        barPaneVBox = new VBox();
        barPane.getChildren().add(barPaneVBox);
        multiRoot.getChildren().add(barPane);
        addMine();
        addChargeBar();
        chargePaneVBox = new VBox();
        chargeBar.getChildren().add(chargePaneVBox);
    }

    private void addMine() {
        mine = new ImageView(new Image(getClass().getResourceAsStream("../resources/MinaGrande.png")));
        mine.setCursor(Cursor.HAND);
        barPaneVBox.getChildren().add(mine);
    }

    private void addChargeBar() {
        bar = new ImageView(new Image(getClass().getResourceAsStream("../resources/chargeBar copia.png")));
        chargeBar.getChildren().add(bar);
        barPaneVBox.getChildren().add(chargeBar);
    }

    public static void createGameGui(int lines, int columns, int numberOfBombs,GameMod gameMod) {
        MultiplayerGui nextView = new MultiplayerGui(lines,columns,numberOfBombs,gameMod);
        Parent window = nextView.createContent();
        Platform.runLater(()->{
            Stage multiStage = MinesweeperLauncher.getPrimaryStage();

            Scene scene = new Scene(window, (columns*25+100), (lines*25+70) );
            multiStage.setScene(scene);
            multiStage.centerOnScreen();
        });
    }


    /**
     * override del metodo si singlePlayerGui, che aggiunge appunto il metodo fillChargeBar
     */
    @Override
    public void refreshScreen(){
        super.refreshScreen();
        fillChargeBar();
    }

    /**
     * riempe la barra di caricamento di disturbo, aggiungendo le immagini delle cariche
     */
    private void fillChargeBar() {
        int i = 0;
        while(i <= getButtonClicked()) {
            chargePaneVBox.getChildren().add(new ImageView(new Image(getClass().getResourceAsStream("../resources/barCharge.png"))));
            barCharges++;
            //System.out.println("BarCharges: " + barCharges + " i : " +i + " Button CLicked : "+ getButtonClicked());
            if (barCharges > 7){
                startBarAnimation();
                startMineAnimation();
                mine.setOnMouseClicked(e -> sendInterference());
                setButtonClicked(0);
            }
            i++;
        }
    }

    /**
     * metodo che invia il disturbo all' avversario
     */
    private void sendInterference() {
        try {
            clientSweeper.sendInterference();
            stopAnimations();
            dischargeBar();
        } catch (RemoteException e) {
            System.out.println("Error Sending disturb");
        }
    }

    public void showInterference(Interferences interferences){
        handleInterference(interferences);
    }

    /**
     * metodo di controllo che sceglie quale bomba premere e chiama loseLife
     */
    private void bombInterference(){
        boolean shotted = false;
        for(int i = 0; i < getLines() ; i++){
            for(int j = 0; j < getColumns() ; j++ ){
                if(getBtn()[i][j].isBomb()) {
                    int shotThis = new Random().nextInt(5);
                    if(shotThis == 1) {
                       // loseLife(i,j);
                        shotted = true;
                    }
                }
            }
        }
        if(!shotted)
            bombInterference();
    }

    /**
     * metodo che fa perdere una vita al giocatore
     */
    private void loseLife(int i , int j) {
        initializeRocket(i,j);
        Platform.runLater(() -> {
            getToolBar().getItems().removeAll(livesImage[lives-1]);
        });

    }

    /**
     * metodo che inizializza e posizione l' immagine del razzo dell razzo
     */
    private void initializeRocket(int i , int j) {
        rocket = new ImageView(new Image(getClass().getResourceAsStream("../resources/Razzo.png")));
        xPos = getBtn()[i][j].getLayoutX();
        yPos = getBtn()[i][j].getLayoutY();
        rocket.setOpacity(0);
        rocket.setLayoutX(xPos);
        rocket.setLayoutY(yPos-100);
        rocket.toFront();
        Platform.runLater(() -> {
            multiRoot.getChildren().add(rocket);
        } );
        startRocketAnimation(i,j);
    }

    /**
     * Lancia l' animazione del razzo
     */
    private void startRocketAnimation(int i , int j) {
        new FadeAnimation(rocket,0,1,Duration.millis(1000)).playAnimation();
        TranslateAnimation tr = new TranslateAnimation(rocket,getBtn()[i][j].getLayoutX(),getBtn()[i][j].getLayoutY(),Duration.millis(1000));
        tr.getTranslateTransition().setOnFinished(e -> {
            getBtn()[i][j].fire();
            Platform.runLater(() -> {
                multiRoot.getChildren().removeAll(rocket);
            });
        });
        tr.playAnimation();
    }

    private void neutralInterference(){
        boolean shotted = false;
        for(int i = 0; i < getLines() ; i++){
            for(int j = 0; j < getColumns() ; j++ ){
                if(!getBtn()[i][j].isBomb()) {
                    int shotThis = new Random().nextInt(7);
                    if(shotThis == 1) {
                        initializeRocket(i,j);
                        shotted = true;
                    }
                }
            }
        }
        if(!shotted)
            bombInterference();
    }

    private void timerInterference(){
        Platform.runLater(() -> {
            getTimerLabel().setText(getTimerLabel().getText()+10);
            startTimerAnimation();
        });
    }

    /**
     * metodo che inizializza e lancia l' animazione del timer, chiamato quando arriva l' interferenza TIMER che aumenta
     * il conteggio di 10 secondi
     */
    private void startTimerAnimation() {
        new RotateAnimation(getTimerLabel(),0,360,Duration.millis(2000)).playAnimation();
        new ScaleAnimation(getTimerLabel(),2,2,Duration.millis(2000)).playAnimation(true);
    }

    /**
     * interferenza che scarica la barra di caricamento interferenza
     */
    private void chargeBarInterference(){

    }

    /**
     * interferenza che ricopre 10 bottoni casuali nella mappa
     */
    private void coverInterference(){
        int covered = 0;
        while(true) {
            if (coverButtons(covered) < 5)
                coverButtons(covered);
            else
                break;
        }
    }

    private int coverButtons(int covered){
        for(int i = 0 ; i< getLines(); i++){
            for( int j = 0 ; j< getColumns(); j++){
                int num = new Random().nextInt(7);
                if(num == 1 && getBtn()[i][j].isClicked() ) {
                    if(covered <5) {
                        covered++;
                        getBtn()[i][j].setClicked(false);
                        getBtn()[i][j].setOpacity(1);
                        getBtn()[i][j].setText("");
                    }
                }
            }
        }
        return covered;
    }


    private void dischargeBar() {
        barCharges = 0;
        chargePaneVBox.getChildren().clear();
    }

    private void stopAnimations() {

        right.getRotateTransition().stop();
    }

    /**
     * metodo che fa partire l' animazione della barra, una volta caricata
     */
    private void startBarAnimation() {
        start = new RotateAnimation(chargeBar,0,5, Duration.millis(250));
        right = new RotateAnimation(chargeBar,-5,5, Duration.millis(500));
        left = new RotateAnimation(chargeBar,5,-5, Duration.millis(500));
        start.getRotateTransition().setOnFinished(e -> left.playAnimation());
        left.getRotateTransition().setOnFinished(e -> right.playAnimation());
        right.getRotateTransition().setOnFinished(e -> left.playAnimation());
        start.playAnimation();
    }

    /**
     * metodo che lancia l' animazione della mina sopra la barra di caricamento
     */
    private void startMineAnimation() {

    }

    @Override
    public void lose(int i , int j){
        lives--;
        if(lives == 0) {
            super.showBombs();
            createGameGui(getLines(), getColumns(), getNumberOfBombs(),super.getGameMod());
            clientSweeper.sendLose();
        }
    }

    public void handleInterference(Object object){
        InterferenceHandler handler = interferenceMap.get(object);
        if(handler != null){
           handler.handle();

        }
    }

    /**
     * metodo chiamato quando l' avversario perde tutte le vite o abbandona
     */
    public void showOtherLose() {
        System.out.println("Laltro ha Perso");
    }

    public void showSurrender() {
        System.out.println("L' altro giocatore ha abbandonato");
    }



    private interface InterferenceHandler{
        void handle() ;
    }
}

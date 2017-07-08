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
import javafx.scene.paint.Color;
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
    private VBox barPaneVBox, chargePaneVBox;
    private ImageView mine, bar;
    private Pane multiRoot;
    private int barCharges = 0;
    private int lives = 3;
    private boolean livesAdded = false;
    private ImageView[] livesImage;
    private ImageView rocket;
    private double xPos, yPos;
    private ClientSweeper clientSweeper;
    private RotateAnimation start, right, left, end;
    private HashMap<Interferences, InterferenceHandler> interferenceMap;

    public MultiplayerGui(int lines, int columns, int numberOfBombs, GameMod gameMod) {
        super(lines, columns, numberOfBombs, gameMod);
        setWIDTH(columns * 25 + 100);
        setHEIGHT(lines * 25 + 70);
        clientSweeper = ClientSweeper.getInstance();
        clientSweeper.setMultiplayerGui(this);
        initializeInterferenceMap();
    }

    private void initializeInterferenceMap() {
        interferenceMap = new HashMap<>();
        interferenceMap.put(Interferences.BOMB, this::bombInterference);
        interferenceMap.put(Interferences.NEUTRAL, this::neutralInterference);
        interferenceMap.put(Interferences.TIMER, this::timerInterference);
        interferenceMap.put(Interferences.CHARGEBAR, this::chargeBarInterference);
        interferenceMap.put(Interferences.COVER, this::coverInterference);
    }

    public void setStarted(boolean started) {
        this.isStarted = started;
    }

    @Override
    public Parent createContent() {
        multiRoot = (Pane) super.createContent();
        addChargeBarPane();
        addLives();
        return multiRoot;
    }

    @Override
    public void addToolbarButtons() {
        getToolBar().getItems().add(homeButton());
        addLives();
        getToolBar().getItems().addAll(this.addDivisorPane(), super.addTimer());
    }

    @Override
    public Button homeButton() {
        super.homeButton();
        getHomeButton().setOnMouseClicked(e -> clientSweeper.sendSurrender());
        return getHomeButton();
    }

    @Override
    public Pane addDivisorPane() {
        Pane divisor = new Pane();
        divisor.setPrefWidth(150);
        divisor.setPrefHeight(30);
        return divisor;
    }

    /**
     * metodo che aggiunge le vite sulla Toolbar
     */
    private void addLives() {
        if (!livesAdded) {
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
    public void startTimer() {
        if (isStarted)
            super.startTimer();
    }

    private void addChargeBarPane() {
        barPane = new Pane();
        chargeBar = new Pane();
        barPane.setLayoutX(getWIDTH() - 80);
        barPane.setLayoutY(getHEIGHT() - 260);
        barPaneVBox = new VBox();
        barPane.getChildren().add(barPaneVBox);
        multiRoot.getChildren().add(barPane);
        addMine();
        addChargeBar();
        chargePaneVBox = new VBox();
        chargePaneVBox.setLayoutX(chargePaneVBox.getLayoutX() + 10);
        chargeBar.getChildren().add(chargePaneVBox);
    }

    private void addMine() {
        mine = new ImageView(new Image(getClass().getResourceAsStream("../resources/MinaGrande.png")));
        mine.setCursor(Cursor.HAND);
        barPaneVBox.getChildren().add(mine);
    }

    private void addChargeBar() {
        bar = new ImageView(new Image(getClass().getResourceAsStream("../resources/chargeBar.png")));
        chargeBar.getChildren().add(bar);
        bar.setFitHeight(200);
        bar.setLayoutX(bar.getX() + 10);
        barPaneVBox.getChildren().add(chargeBar);
    }

    public static void createGameGui(int lines, int columns, int numberOfBombs, GameMod gameMod) {
        MultiplayerGui nextView = new MultiplayerGui(lines, columns, numberOfBombs, gameMod);
        Parent window = nextView.createContent();
        Platform.runLater(() -> {
            Stage multiStage = MinesweeperLauncher.getPrimaryStage();

            Scene scene = new Scene(window, (columns * 25 + 100), (lines * 25 + 70));
            multiStage.setScene(scene);
            multiStage.centerOnScreen();
        });
    }


    /**
     * override del metodo si singlePlayerGui, che aggiunge appunto il metodo fillChargeBar
     */
    @Override
    public void refreshScreen() {
        super.refreshScreen();
        fillChargeBar();
    }

    /**
     * riempe la barra di caricamento di disturbo, aggiungendo le immagini delle cariche
     */
    private void fillChargeBar() {
        if (barCharges > 8) {
            return;
        }
        int i = 0;
        System.out.println("Button clicked multi : " + getButtonClicked());
        while (i < getButtonClicked()) {
            chargePaneVBox.getChildren().add(new ImageView(new Image(getClass().getResourceAsStream("../resources/barCharge.png"))));
            barCharges++;
            System.out.println("BarCharges: " + barCharges + " i : " + i + " Button CLicked : " + getButtonClicked());
            if (barCharges > 8) {
                startBarAnimation();
                startMineAnimation();
                mine.setOnMouseClicked(e -> sendInterference());
                setButtonClicked(0);
                return;
            }
            i++;
        }
    }

    /**
     * metodo che invia il disturbo all' avversario
     */
    private void sendInterference() {
        try {
            System.out.println("Chiamo sendInterference ");
            clientSweeper.sendInterference();
            stopAnimations();
            dischargeBar();
        } catch (RemoteException e) {
            System.out.println("Error Sending disturb");
        }
    }

    public void showInterference(Interferences interferences) {
        handleInterference(interferences);
    }

    /**
     * metodo di controllo che sceglie quale bomba premere e chiama loseLife
     */
    private void bombInterference() {
        System.out.println("Sono in bomb interference");
        boolean shotted = false;
        for (int i = 0; i < getLines(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                if (getBtn()[i][j].isBomb()) {
                    int shotThis = new Random().nextInt(5);
                    if (shotThis == 1) {
                        //initializeRocket(i,j);
                        loseLife(i, j);
                        shotted = true;
                        return;
                    }
                }
            }
        }
        if (!shotted)
            bombInterference();
    }

    /**
     * metodo che fa perdere una vita al giocatore
     */
    private void loseLife(int i, int j) {
        Platform.runLater(() -> {
            lives--;
            new ScaleAnimation(livesImage[lives - 1], 1.5, 1.5, Duration.millis(1000)).playAnimation();
            new FadeAnimation(livesImage[lives - 1], 1, 0, Duration.millis(3000)).playAnimation();
        });
    }

    /**
     * metodo che inizializza e posizione l' immagine del razzo dell razzo
     */
    private void initializeRocket(int i, int j) {
        rocket = new ImageView(new Image(getClass().getResourceAsStream("../resources/Razzo.png")));
        xPos = getBtn()[i][j].getLayoutX();
        yPos = getBtn()[i][j].getLayoutY();
        rocket.setOpacity(0);
        rocket.setLayoutX(xPos);
        rocket.setLayoutY(yPos - 100);
        rocket.toFront();
        Platform.runLater(() -> {
            multiRoot.getChildren().add(rocket);
        });
        startRocketAnimation(i, j);
    }

    /**
     * Lancia l' animazione del razzo
     */
    private void startRocketAnimation(int i, int j) {
        new FadeAnimation(rocket, 0, 1, Duration.millis(1000)).playAnimation();
        TranslateAnimation tr = new TranslateAnimation(rocket, getBtn()[i][j].getLayoutX(), getBtn()[i][j].getLayoutY(), Duration.millis(1000));
        tr.getTranslateTransition().setOnFinished(e -> {
            getBtn()[i][j].fire();
            Platform.runLater(() -> {
                multiRoot.getChildren().removeAll(rocket);
            });
        });
        tr.playAnimation();
    }

    private void neutralInterference() {
        boolean shotted = false;
        for (int i = 0; i < getLines(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                if (!getBtn()[i][j].isBomb()) {
                    int shotThis = new Random().nextInt(7);
                    if (shotThis == 1) {
                        shotThis(i, j);
                        return;
                    }
                }
            }
        }
        if (!shotted)
            bombInterference();
    }

    private void shotThis(int i, int j) {
        //initializeRocket(i,j);
        Platform.runLater(() -> {
            getBtn()[i][j].fire();
        });
    }

    private void timerInterference() {
        Platform.runLater(() -> {
            getTimerLabel().setText(String.valueOf(Integer.parseInt(getTimerLabel().getText()) + 10));
            startTimerAnimation();
        });
    }

    /**
     * metodo che inizializza e lancia l' animazione del timer, chiamato quando arriva l' interferenza TIMER che aumenta
     * il conteggio di 10 secondi
     */
    private void startTimerAnimation() {
        new RotateAnimation(getTimerLabel(), 0, 360, Duration.millis(2000)).playAnimation();
        new ScaleAnimation(getTimerLabel(), 2, 2, Duration.millis(2000)).playAnimation(true);
    }

    /**
     * interferenza che scarica la barra di caricamento interferenza
     */
    private void chargeBarInterference() {
        Platform.runLater(() -> {
            new ScaleAnimation(chargeBar, 1.5, 1.5, Duration.millis(1000)).playAnimation(true);
            dischargeBar();
        });
    }

    /**
     * interferenza che ricopre 10 bottoni casuali nella mappa
     */
    private void coverInterference() {
        int covered = 0;
        while (true) {
            if (coverButtons(covered) < 5) {
                Platform.runLater(() -> {
                    coverButtons(covered);
                });
            } else
                break;
        }
    }

    private int coverButtons(int covered) {
        for (int i = 0; i < getLines(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                int num = new Random().nextInt(7);
                if (num == 1 && getBtn()[i][j].isClicked()) {
                    if (covered < 5) {
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

        right.getRotateTransition().setOnFinished(e -> new RotateAnimation(chargeBar, 5, 0, Duration.millis(250)).playAnimation());
        left.getRotateTransition().setOnFinished(e -> new RotateAnimation(chargeBar, -5, 0, Duration.millis(250)).playAnimation());
    }

    /**
     * metodo che fa partire l' animazione della barra, una volta caricata
     */
    private void startBarAnimation() {
        start = new RotateAnimation(chargeBar, 0, 5, Duration.millis(250));
        right = new RotateAnimation(chargeBar, -5, 5, Duration.millis(500));
        left = new RotateAnimation(chargeBar, 5, -5, Duration.millis(500));
        start.getRotateTransition().setOnFinished(e -> left.playAnimation());
        left.getRotateTransition().setOnFinished(e -> right.playAnimation());
        right.getRotateTransition().setOnFinished(e -> left.playAnimation());
        start.playAnimation();
    }

    /**
     * metodo che lancia l' animazione della mina sopra la barra di caricamento
     */
    private void startMineAnimation() {
        new RotateAnimation(mine, 0, 360, Duration.millis(2000)).playAnimation();
    }

    @Override
    public void win(){
        super.win();
    }

    @Override
    public void lose(int i, int j) {
        loseLife(i, j);
        if (lives == 0) {
            super.showBombs();
            backToHome();
            clientSweeper.sendLose();
        }
    }

    private void backToHome() {
        homeButton().fire();
    }

    public void handleInterference(Object object) {
        InterferenceHandler handler = interferenceMap.get(object);
        if (handler != null) {
            handler.handle();

        }
    }

    /**
     * metodo chiamato quando l' avversario perde tutte le vite o abbandona
     */
    public void showOtherLose() {

        Platform.runLater(() -> {
            super.startRemoveAnimation();
            removeChargeBar();
            Label youWin = new Label("YOU WIN");
            youWin.setFont(new Font("AR JULIAN",25));
            youWin.setTextFill(Color.WHITESMOKE);
            youWin.setLayoutX(getWIDTH()/2-getWIDTH()/3);
            youWin.setLayoutY(getHEIGHT()/2);
            multiRoot.getChildren().add(youWin);
        });
    }

    private void removeChargeBar() {
        multiRoot.getChildren().removeAll(chargeBar);
    }

    public void showSurrender() {
        System.out.println("L' altro giocatore ha abbandonato");
    }



    private interface InterferenceHandler{
        void handle() ;
    }
}

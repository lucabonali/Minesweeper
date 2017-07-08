package client.GUI.controllers;

import api.GameMod;
import client.GUI.MinesweeperLauncher;
import client.GUI.animations.FadeAnimation;
import client.GUI.gameGui.MultiplayerGui;
import client.GUI.gameGui.SinglePlayerGui;
import client.GUI.animations.ScaleAnimation;
import client.clientConnection.ClientSweeper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

/**
 * @author Luca
 */
public class LauncherController implements Initializable {
    static final int EASY_BOMBS = 12;
    static final int MEDIUM_BOMBS = 30;
    static final int HARD_BOMBS = 70;
    static final int EASY_LINES_COLUMNS = 9;
    static final int MEDIUM_LINES_COLUMNS = 15;
    static final int HARD_LINES = 15;
    static final int HARD_COLUMNS = 30;

    @FXML
    public Button starbutton;
    @FXML
    private Button loginButton;
    @FXML
    private Button iconifyButton;
    @FXML
    private RadioButton easy, medium, hard;
    @FXML
    private Button onlineButton;
    @FXML
    private Button menuButton;
    @FXML
    private Button startButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Button buttonExit;

    static boolean isLogged;
    private FadeAnimation fadeIn,fadeOut;
    private ScaleAnimation big,small;
    private int lines, columns, numberOfBombs;

    private GameMod gameMod = GameMod.EASY;

    /**
     * metodo che chiude la finestra quando viene premuto il pulsante di chiusura
     * @param actionEvent
     */
    public void closeWindow(ActionEvent actionEvent) {
        MinesweeperLauncher.getPrimaryStage().close();
    }

    public void closeMouseEntered(MouseEvent mouseEvent) {
        buttonExit.setCursor(Cursor.HAND);
    }

    /**
     * metodo chiamato quando l' utente preme il pulsante di inizio della partita
     * @param actionEvent
     */
    public void startGame(ActionEvent actionEvent) {
        try {
            SinglePlayerGui.createGameGui(lines,columns, numberOfBombs,gameMod);
            System.out.println(" Liene "+lines+"COLonne "+columns+" bombe "+numberOfBombs);
        } catch (InterruptedException | UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * metodo che inizia la partita multigiocatore, se è stato effettuato il login e se premuto sul pulsante MultiPlayer
     * @param actionEvent
     */
    public void startMultiGame(ActionEvent actionEvent) {
        if(isLogged) {
                try {
                    if(medium.isSelected()) {
                        ClientSweeper.getInstance().createGame(GameMod.MEDIUM);
                        System.out.println("Creo una partita media:");
                    }
                    else if(hard.isSelected())
                        ClientSweeper.getInstance().createGame(GameMod.HARD);
                    else  {
                        ClientSweeper.getInstance().createGame(GameMod.EASY);
                        System.out.println("Creo una partita easy:");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                    System.out.println("ERROR CREATING MULTIPLAYER GAME");
                }
            MultiplayerGui.createGameGui(lines, columns, numberOfBombs,gameMod);
        }
        else
            loginButton.fire();
    }

    /**
     * metodo che parte all' inizio, quando l' oggetto controller viene creato, e si occupa di settare i valori iniziali
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lines = EASY_LINES_COLUMNS;
        columns = EASY_LINES_COLUMNS;
        numberOfBombs = EASY_BOMBS;
        setCursor();
        initializeFadeAnimation();
        initializeScaleAnimation();
        initializeMenuDifficulties();
    }

    /**
     * inizializza i Radio Button della scelta della difficoltà
     */
    private void initializeMenuDifficulties() {
        ToggleGroup toggleGroup = new ToggleGroup();
        easy.setToggleGroup(toggleGroup);
        medium.setToggleGroup(toggleGroup);
        hard.setToggleGroup(toggleGroup);
    }

    /**
     * metodo che inizializza l' immagine del cursore
     */
    private void setCursor() {
        onlineButton.setCursor(Cursor.HAND);
        startButton.setCursor(Cursor.HAND);
    }

    private void initializeScaleAnimation() {
        big = new ScaleAnimation(titleLabel,1.2,1.2,Duration.millis(1000));
        small = new ScaleAnimation(titleLabel,1,1,Duration.millis(1000));
        big.getScaleTransition().setOnFinished(e -> small.playAnimation(false));
        small.getScaleTransition().setOnFinished(e -> big.playAnimation(false));
        big.playAnimation(false);
    }


    private void initializeFadeAnimation() {
        fadeIn = new FadeAnimation(titleLabel,0.5,1, Duration.millis(1000));
        fadeOut = new FadeAnimation(titleLabel,1,0.5, Duration.millis(1000));
        fadeIn.getFadeTransition().setOnFinished(e -> fadeOut.playAnimation());
        fadeOut.getFadeTransition().setOnFinished(e -> fadeIn.playAnimation());
        fadeIn.playAnimation();
    }

    public void zoomButton(MouseEvent mouseEvent) {

    }

    /**
     * metodo che mostra a schermo i tre livelli di difficolta
     * @param actionEvent
     */
    public void showDifficulties(ActionEvent actionEvent) {
        new FadeAnimation(easy,0,1,Duration.millis(1000)).playAnimation();
        new FadeAnimation(medium,0,1,Duration.millis(1000)).playAnimation();
        new FadeAnimation(hard,0,1,Duration.millis(1000)).playAnimation();
    }

    private void hideDifficulties(){
        new FadeAnimation(easy,1,0,Duration.millis(1000)).playAnimation();
        new FadeAnimation(medium,1,0,Duration.millis(1000)).playAnimation();
        new FadeAnimation(hard,1,0,Duration.millis(1000)).playAnimation();
    }

    public void selectEasy(ActionEvent actionEvent) {
        lines = EASY_LINES_COLUMNS;
        columns = EASY_LINES_COLUMNS;
        numberOfBombs = EASY_BOMBS;
        gameMod = GameMod.EASY;
        hideDifficulties();
    }

    public void selectMedium(ActionEvent actionEvent) {
        lines = MEDIUM_LINES_COLUMNS;
        columns = MEDIUM_LINES_COLUMNS;
        numberOfBombs = MEDIUM_BOMBS;
        gameMod = GameMod.MEDIUM;
        hideDifficulties();
    }

    public void selectHard(ActionEvent actionEvent) {
        lines = HARD_LINES;
        columns = HARD_COLUMNS;
        numberOfBombs = HARD_BOMBS;
        gameMod = GameMod.HARD;
        hideDifficulties();
    }

    public void zoomIn(MouseEvent mouseEvent) {
        new ScaleAnimation((Node)mouseEvent.getSource(),1.5,1.5,Duration.millis(500)).playAnimation(false);
    }

    public void zoomOut(MouseEvent mouseEvent) {
        new ScaleAnimation((Node)mouseEvent.getSource(),1,1,Duration.millis(500)).playAnimation(false);
    }

    public void iconify(ActionEvent actionEvent) {
        MinesweeperLauncher.getPrimaryStage().setIconified(true);
    }


    public void showLogin(ActionEvent actionEvent) {
        if(!isLogged) {
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

    public void showScoreScreen(ActionEvent actionEvent) {
        if (isLogged) {
            Stage scores = new Stage();
            Platform.runLater(() -> {
                Parent scoreScene = null;
                try {
                    scoreScene = FXMLLoader.load(getClass().getResource("../fxml/scores.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                scores.setScene(new Scene(scoreScene, 413, 232));
                scores.centerOnScreen();
                scores.setOnCloseRequest(e -> System.exit(0));
                scores.initStyle(StageStyle.UNDECORATED);
                scores.show();
            });
        }
        else{
            loginButton.fire();
        }
    }

}

package client.GUI.controllers;

import client.GUI.MinesweeperLauncher;
import client.GUI.animations.FadeAnimation;
import client.GUI.gameGui.GameGui;
import client.GUI.animations.ScaleAnimation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
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


    private FadeAnimation fadeIn,fadeOut;
    private ScaleAnimation big,small;
    private int lines, columns, numberOfBombs;

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
            GameGui.createGameGui(lines,columns, numberOfBombs);
        } catch (InterruptedException | UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * metodo che parte all' inizio, quando l' oggetto controller viene creato, e si occupa di settare i valori iniziali
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lines = 9;
        columns = 9;
        numberOfBombs = 15;
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
        hideDifficulties();
    }

    public void selectMedium(ActionEvent actionEvent) {
        lines = MEDIUM_LINES_COLUMNS;
        columns = MEDIUM_LINES_COLUMNS;
        numberOfBombs = MEDIUM_BOMBS;
        hideDifficulties();
    }

    public void selectHard(ActionEvent actionEvent) {
        lines = HARD_LINES;
        columns = HARD_COLUMNS;
        numberOfBombs = HARD_BOMBS;
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
}

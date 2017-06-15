package GUI.controllers;

import GUI.MinesweeperLauncher;
import GUI.animations.FadeAnimation;
import GUI.animations.ScaleAnimation;
import GUI.gameGui.GameGui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Luca
 */
public class LauncherController implements Initializable {
    @FXML
    private Button startButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Button buttonExit;


    private FadeAnimation fadeIn,fadeOut;
    private ScaleAnimation big,small;
    private int lines, columns;

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
            GameGui.createGameGui(lines,columns);
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
        initializeFadeAnimation();
        initializeScaleAnimation();
    }

    private void initializeScaleAnimation() {
        big = new ScaleAnimation(titleLabel,1.2,1.2,Duration.millis(1000));
        small = new ScaleAnimation(titleLabel,1,1,Duration.millis(1000));
        big.getScaleTransition().setOnFinished(e -> small.playAnimation());
        small.getScaleTransition().setOnFinished(e -> big.playAnimation());
        big.playAnimation();
    }


    private void initializeFadeAnimation() {
        fadeIn = new FadeAnimation(titleLabel,0.5,1, Duration.millis(1000));
        fadeOut = new FadeAnimation(titleLabel,1,0.5, Duration.millis(1000));
        fadeIn.getFadeTransition().setOnFinished(e -> fadeOut.playAnimation());
        fadeOut.getFadeTransition().setOnFinished(e -> fadeIn.playAnimation());
        fadeIn.playAnimation();
    }
}

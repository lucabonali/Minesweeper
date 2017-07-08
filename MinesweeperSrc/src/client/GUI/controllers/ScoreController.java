package client.GUI.controllers;

import api.GameMod;
import client.clientConnection.ClientSweeper;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import server.ScoreResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Luca
 */
public class ScoreController implements Initializable {

    @FXML
    private VBox easyBox,mediumBox,hardBox;
    @FXML
    private Button homeButton;
    private List<String> easyNames,mediumNames, hardNames;
    private List<Integer> easyTime,mediumTime,hardTime;
    private List<Label> easyLabel, mediumLabel,hardLabel;


    public void backToHome(ActionEvent actionEvent) {
        homeButton.getScene().getWindow().hide();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        easyNames = new ArrayList<>();
        easyTime = new ArrayList<>();
        mediumNames = new ArrayList<>();
        mediumTime = new ArrayList<>();
        hardNames = new ArrayList<>();
        hardTime = new ArrayList<>();
        initializeLabelList();
        setScores();
    }

    private void initializeLabelList() {
        easyLabel = new ArrayList<>();
        mediumLabel = new ArrayList<>();
        hardLabel = new ArrayList<>();
    }

    private void setScores() {
            ClientSweeper.getInstance().getScores(GameMod.EASY);
            easyNames = ClientSweeper.getInstance().getScoreNames();
            easyTime = ClientSweeper.getInstance().getScoreTimes();
            ClientSweeper.getInstance().getScores(GameMod.MEDIUM);
            mediumNames = ClientSweeper.getInstance().getScoreNames();
            mediumTime = ClientSweeper.getInstance().getScoreTimes();
            ClientSweeper.getInstance().getScores(GameMod.HARD);
            hardNames = ClientSweeper.getInstance().getScoreNames();
            hardTime = ClientSweeper.getInstance().getScoreTimes();
            showScores();
    }


    private void showScores() {
        for(int i = 0 ; i < easyNames.size() ; i++){
            if(easyNames.get(i) != null) {
                easyLabel.add(new Label("" + (i + 1) + "^ - " + easyNames.get(i) + " : " + easyTime.get(i)));
                easyLabel.get(i).setFont(new Font("AR JULIAN", 13));
                easyLabel.get(i).setPrefHeight(30);
                easyLabel.get(i).setTextFill(Color.WHITESMOKE);
                easyBox.getChildren().add(easyLabel.get(i));
            }
        }
        for(int i = 0 ; i < mediumNames.size() ; i++){
            if(mediumNames.get(i) != null) {
                mediumLabel.add(new Label("" + (i + 1) + "^ - " + mediumNames.get(i) + " : " + mediumTime.get(i)));
                mediumLabel.get(i).setFont(new Font("AR JULIAN", 13));
                mediumLabel.get(i).setPrefHeight(30.1);
                mediumLabel.get(i).setTextFill(Color.WHITESMOKE);
                mediumBox.getChildren().add(mediumLabel.get(i));
            }
        }
        for(int i = 0 ; i < hardNames.size() ; i++){
            if(hardNames.get(i) != null) {
                hardLabel.add(new Label("" + (i + 1) + "^ - " + hardNames.get(i) + " : " + hardTime.get(i)));
                hardLabel.get(i).setFont(new Font("AR JULIAN", 13));
                hardLabel.get(i).setPrefHeight(29.9);
                hardLabel.get(i).setTextFill(Color.WHITESMOKE);
                hardBox.getChildren().add(hardLabel.get(i));
            }
        }
        fillEmptyLabels();
    }

    private void fillEmptyLabels() {

        if(easyNames.size() < 5){
            int k = easyNames.size();
            while (k < 5){
                easyLabel.add(new Label(" ---------- "));
                easyLabel.get(k).setFont(new Font("AR JULIAN", 13));
                easyLabel.get(k).setPrefHeight(30);
                easyLabel.get(k).setTextFill(Color.WHITESMOKE);
                easyBox.getChildren().add(easyLabel.get(k));
                k++;
            }
        }
        if(mediumNames.size() < 5){
            int k = mediumNames.size();
            while (k < 5){
                mediumLabel.add(new Label(" ---------- "));
                mediumLabel.get(k).setFont(new Font("AR JULIAN", 13));
                mediumLabel.get(k).setPrefHeight(30.1);
                mediumLabel.get(k).setTextFill(Color.WHITESMOKE);
                mediumBox.getChildren().add(mediumLabel.get(k));
                k++;
            }
        }
        if(hardNames.size() < 5){
            int k = hardNames.size();
            while (k < 5){
                hardLabel.add(new Label(" ---------- "));
                hardLabel.get(k).setFont(new Font("AR JULIAN", 13));
                hardLabel.get(k).setPrefHeight(29.9);
                hardLabel.get(k).setTextFill(Color.WHITESMOKE);
                hardBox.getChildren().add(hardLabel.get(k));
                k++;
            }
        }
    }
}

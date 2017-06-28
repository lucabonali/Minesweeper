package client.GUI.controllers;

import client.GUI.alerts.LoginAlert;
import client.clientConnection.ClientSweeper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

/**
 * @author Luca
 */
public class LoginController implements Initializable {

    @FXML
    private TextField userName,password;
    @FXML
    private Button submitButton;
    @FXML
    private Button homeButton;

    private ClientSweeper clientSweeper;



    public void backToHome(ActionEvent actionEvent) {
        backToLauncher();
    }
    public void backToLauncher(){
        homeButton.getScene().getWindow().hide();
    }

    public void login(ActionEvent actionEvent) {
        if(!checkFields()) {
            LoginAlert loginAlert = new LoginAlert(Alert.AlertType.ERROR, "");
            return;
        }
        else{
            ClientSweeper clientSweeper = null;
            try {
                clientSweeper = ClientSweeper.createInstance(userName.getText(),password.getText());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            clientSweeper.login(userName.getText(),password.getText());
            LauncherController.isLogged = true;
            backToLauncher();
        }


    }

    private boolean checkFields() {
        if(userName.getText().equals(null))
            return false;
        if(password.getText().equals(null))
            return false;
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

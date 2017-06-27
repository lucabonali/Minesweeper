package client.GUI.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * @author Luca
 */
public class LoginAlert extends Alert {


    public LoginAlert(AlertType alertType) {
        super(alertType);
    }

    public LoginAlert(AlertType alertType, String contentText, ButtonType... buttons) {
        super(alertType, contentText, buttons);
    }


}

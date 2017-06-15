package GUI;/**
 * @author Andrea
 * @author Luca
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MinesweeperLauncher extends Application {
    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * metodo che prende in ingresso lo stage del gioco e lo lancia
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/launcher.fxml"));
            stage.setScene(new Scene(root, 500, 275));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.centerOnScreen();
            stage.setOnCloseRequest(e -> System.exit(0));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("resources/IconaBomba.png")));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage(){
        return stage;
    }



}

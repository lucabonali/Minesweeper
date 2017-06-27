package client.GUI.gameGui;

import client.GUI.MinesweeperLauncher;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;



/**
 * * @author Luca
 */
public class MultiplayerGui extends SinglePlayerGui {
    private Pane chargeBar;
    private ImageView mine;
    private Pane multiRoot;

    public MultiplayerGui(int lines, int columns, int numberOfBombs){
        super(lines,columns,numberOfBombs);
        setWIDTH(columns*25+100);
        setHEIGHT(lines*25+70);
    }


    @Override
    public Parent createContent() {
        multiRoot = (Pane) super.createContent();
        addChargeBar();
        addMine();
        return multiRoot ;
    }

    private void addMine() {
        mine = new ImageView(new Image(getClass().getResourceAsStream("../resources/MinaGrande.png")));
        mine.setLayoutX(-70);
        mine.setLayoutY(-280);
        mine.setFitWidth(70);
        mine.setFitHeight(70);
        multiRoot.getChildren().add(mine);
    }

    private void addChargeBar() {
        chargeBar = new Pane();
        chargeBar.setPrefSize(30,100);
        chargeBar.setLayoutX(getWIDTH()-60);
        chargeBar.setLayoutY(getHEIGHT()-245);
        ImageView bar = new ImageView(new Image(getClass().getResourceAsStream("../resources/chargeBar copia.png")));
        chargeBar.getChildren().add(bar);
        multiRoot.getChildren().add(chargeBar);
    }

    public static void createGameGui(int lines, int columns, int numberOfBombs) {
        MultiplayerGui nextView = new MultiplayerGui(lines,columns,numberOfBombs);
        Parent window = nextView.createContent();
        Platform.runLater(()->{
            Stage multiStage = MinesweeperLauncher.getPrimaryStage();

            Scene scene = new Scene(window, (columns*25+100), (lines*25+70) );
            multiStage.setScene(scene);
            multiStage.centerOnScreen();
        });
    }


}

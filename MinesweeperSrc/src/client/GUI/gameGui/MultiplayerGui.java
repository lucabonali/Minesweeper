package client.GUI.gameGui;

import client.GUI.MinesweeperLauncher;
import client.GUI.animations.RotateAnimation;
import client.clientConnection.ClientSweeper;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.rmi.RemoteException;


/**
 * * @author Luca
 */
public class MultiplayerGui extends SinglePlayerGui {
    private Pane barPane;
    private Pane chargeBar;
    private VBox barPaneVBox,chargePaneVBox;
    private ImageView mine,bar;
    private Pane multiRoot;
    private int barCharges = 0;
    private ClientSweeper clientSweeper;
    private RotateAnimation start,right,left,end;

    public MultiplayerGui(int lines, int columns, int numberOfBombs){
        super(lines,columns,numberOfBombs);
        setWIDTH(columns*25+100);
        setHEIGHT(lines*25+70);
        clientSweeper = ClientSweeper.getInstance();
    }


    @Override
    public Parent createContent() {
        multiRoot = (Pane) super.createContent();
        addChargeBarPane();
        return multiRoot ;
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
            System.out.println("BarCharges: " + barCharges + " i : " +i + " Button CLicked : "+ getButtonClicked());
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
    public void lose(){
        super.showBombs();
        createGameGui(getLines(),getColumns(),getNumberOfBombs());
    }

}

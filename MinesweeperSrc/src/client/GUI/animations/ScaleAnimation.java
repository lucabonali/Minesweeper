package client.GUI.animations;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;


/**
 * @author Luca
 */
public class ScaleAnimation {
    private Node node;

    private ScaleTransition scaleTransition;
    private Duration duration;

    /**
     * metodo che inizializza la transizione ingrandimento o rimpicciolimento
     * @param node
     * @param x
     * @param y
     * @param duration
     */
    public ScaleAnimation(Node node, double x, double y, Duration duration){
        this.node = node;
        this.duration = duration;
        scaleTransition = new ScaleTransition();
        scaleTransition.setDuration(duration);
        scaleTransition.setNode(node);
        scaleTransition.setToX(x);
        scaleTransition.setToY(y);
    }

    public ScaleTransition getScaleTransition() {
        return scaleTransition;
    }

    /**
     * fa partire la transizione quando richiamato
     */
    public void playAnimation(){

        scaleTransition.play();
        scaleTransition.setOnFinished(e -> {
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.setDuration(duration);
            scaleTransition.play();
        } );
    }



}

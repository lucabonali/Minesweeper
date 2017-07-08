package client.GUI.animations;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * @author Luca
 */
public class TranslateAnimation implements SweeperAnimation {
    private Node node;

    private TranslateTransition translateTransition;

    public TranslateAnimation(Node node, double toX, double toY, Duration duration){
        this.node = node;
        translateTransition = new TranslateTransition();
        translateTransition.setNode(node);
        translateTransition.setToX(toX);
        translateTransition.setToY(toY);
        translateTransition.setDuration(duration);

    }

    public TranslateTransition getTranslateTransition() {
        return translateTransition;
    }

    public void playAnimation(){
        translateTransition.play();
    }
}

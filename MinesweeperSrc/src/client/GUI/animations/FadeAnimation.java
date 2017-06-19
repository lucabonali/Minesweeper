package client.GUI.animations;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * @author Luca
 */
public class FadeAnimation {
    private Node node;

    private FadeTransition fadeTransition;

    public FadeAnimation(Node node, double from, double to, Duration duration){
        this.node = node;
        fadeTransition = new FadeTransition();
        fadeTransition.setNode(node);
        fadeTransition.setFromValue(from);
        fadeTransition.setToValue(to);
        fadeTransition.setDuration(duration);

    }

    public FadeTransition getFadeTransition() {
        return fadeTransition;
    }

    public void playAnimation(){
        fadeTransition.play();
    }
}

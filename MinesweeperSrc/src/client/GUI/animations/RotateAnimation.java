package client.GUI.animations;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * @author Luca
 */
public class RotateAnimation implements SweeperAnimation {
    private Node node;

    private RotateTransition rotateTransition;

    public RotateAnimation(Node node, double from, double to, Duration duration){
        this.node = node;
        rotateTransition = new RotateTransition();
        rotateTransition.setNode(node);
        rotateTransition.setFromAngle(from);
        rotateTransition.setToAngle(to);
        rotateTransition.setDuration(duration);

    }

    public RotateTransition getRotateTransition() {
        return rotateTransition;
    }

    public void playAnimation(){
        rotateTransition.play();
    }
}

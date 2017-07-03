package client.GUI.gameGui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Luca
 */
public class Themes {
    private ImageView flag;
    private Image background;

    public Themes(Image background, ImageView flag){

    }

    public Image getBackground() {
        return background;
    }

    public void setBackground(Image background) {
        this.background = background;
    }

    public ImageView getFlag() {
        return flag;
    }

    public void setFlag(ImageView flag) {
        this.flag = flag;
    }




}

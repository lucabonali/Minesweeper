import javafx.scene.control.Button;

/**
 * Created by Luca on 13/05/2017.
 */
public class Btn extends Button {
    private int id,nearBombs;
    private boolean clicked,flaged,bomb;

    public Btn(int id){
        this.id = id;
    }
}

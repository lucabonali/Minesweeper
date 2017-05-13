import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Created by Luca on 13/05/2017.
 */
public class ButtonHandler implements EventHandler<ActionEvent> {
    private Btn button;

    public ButtonHandler(Btn button){
        this.button = button;
    }

    @Override
    public void handle(ActionEvent event) {
        button.setClicked(true);
        if(button.isBomb())
            lose();
        else
            openGrid();
    }

    private void openGrid() {
        //Da implementare
    }

    private void lose() {
        //Da implementare
    }


}

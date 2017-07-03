package api;

import java.util.HashMap;

/**
 * @author Luca
 */
public enum GameMod {
    EASY("EASY"),MEDIUM("MEDIUM"),HARD("HARD");

    private String code;

    GameMod(String s){
        code = s ;
    }

    @Override
    public String toString() {
        return code;
    }
}

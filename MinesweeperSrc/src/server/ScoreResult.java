package server;

import api.GameMod;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Luca
 */
public class ScoreResult {
    private List<String> nameList;
    private List<Integer> timeList;

    public ScoreResult(ResultSet scoreResult){
        nameList = new ArrayList<>();
        timeList = new ArrayList<>();
        fillLists(scoreResult);
    }

    public List<String> getNamesList() {
        return nameList;
    }

    public List<Integer> getTimeList() {
        return timeList;
    }

    /**
     * metodo che prende in ingresso il resultset e lo spezzetta per ottenere la lista dei nomi e la lista dei tempi
     * dei migliori giocatori
     */
    private void fillLists(ResultSet scoreResult) {
        try {
            if(scoreResult != null){
                while(scoreResult.next()){
                    nameList.add(scoreResult.getString("name"));
                    timeList.add(scoreResult.getInt("time"));
                }
                scoreResult.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

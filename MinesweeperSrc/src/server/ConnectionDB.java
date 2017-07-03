package server;

import api.GameMod;

import java.sql.*;

/**
 * Classe che si interfaccia con il database
 * @author Luca
 */
public class ConnectionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/minesweeperDB";
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String USERNAME_DB = "root";
    private static final String PASSWORD_DB = "";
    private static final String QUERY_INSERT_GAME = "INSERT INTO games (name,time,gamemod) VALUES (?,?,?) ";
    private static final String QUERY_GET_GAMES = "SELECT name,time FROM games WHERE gamemod = ";
    private static final String QUERY_ORDER = " ORDER BY time";
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;


    public ConnectionDB() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(URL, USERNAME_DB, PASSWORD_DB);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
          e.printStackTrace();
        }
    }

    /**
     * metodo che mi esegue la query al database
     * @param gameMod query da eseguire
     * @return il result set, risultato dell'interrogazione al db
     */
    public ResultSet executeQuery(GameMod gameMod) {
        String query = QUERY_GET_GAMES+"'"+gameMod.toString()+"'"+QUERY_ORDER;
        System.out.println(query);
        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void insert(String name, int time , String gameMod){
        String qInsert = QUERY_INSERT_GAME;
        try {
            PreparedStatement prepQ = connection.prepareStatement(qInsert);
            prepQ.setString(1,name);
            prepQ.setInt(2,time);
            prepQ.setString(3,gameMod);
            prepQ.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * metodo che mi chiude la connessione col database
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

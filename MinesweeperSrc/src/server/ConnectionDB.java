package server;

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
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;


    public ConnectionDB() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(URL, USERNAME_DB, PASSWORD_DB);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("ERROR CREATING CONNECTION TO DATABASE!!");
        }
    }

    /**
     * metodo che mi esegue la query al database
     * @param query query da eseguire
     * @return il result set, risultato dell'interrogazione al db
     */
    public ResultSet executeQuery(String query) {
        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("ERROR EXECUTING QUERY !");
        }
        return resultSet;
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

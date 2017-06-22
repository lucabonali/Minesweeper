package server;

import api.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Andrea
 * @author Luca
 */
public class ServerRemote extends UnicastRemoteObject implements ServerInterface {
    private ConnectionDB connectionDB;
    private static final String QUERY_LOGIN = "";


    protected ServerRemote() throws RemoteException {
        connectionDB = new ConnectionDB();
    }

    @Override
    public boolean login(String username, String password) throws RemoteException {
    return true;
    }
}
package server;

import api.ClientInterface;
import api.GameMod;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Luca
 */
public class ClientRemote extends UnicastRemoteObject implements ClientInterface {

    protected ClientRemote(int port) throws RemoteException {
        super(port);
    }


    @Override
    public boolean createGame(GameMod gameMod) throws RemoteException {
        return false;
    }
}

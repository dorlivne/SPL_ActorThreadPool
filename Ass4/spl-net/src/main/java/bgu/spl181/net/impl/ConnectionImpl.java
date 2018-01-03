package bgu.spl181.net.impl;

import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.BlockingConnectionHandler;
import bgu.spl181.net.srv.bidi.ConnectionHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionImpl<T> implements Connections{

    private ConcurrentHashMap<Integer,ConnectionHandler> connectionsDataBase = new ConcurrentHashMap<>();

    @Override
    public boolean send(int connectionId, Object msg) {
        if (!this.connectionsDataBase.contains(connectionId))
            return false;
        else {//exists
            this.connectionsDataBase.get(connectionId).send(msg);
            return true;
        }//end of else
    }

    @Override
    public void broadcast(Object msg) {

        Collection<ConnectionHandler> ActiveClients = this.connectionsDataBase.values();
        for (ConnectionHandler activeClient : ActiveClients) {
                activeClient.send(msg);
        }
    }

    @Override
    public void disconnect(int connectionId) {

        if (this.connectionsDataBase.contains(connectionId)) {
            ConnectionHandler disconnect = this.connectionsDataBase.get(connectionId);
            try {
                disconnect.close();
            }catch (IOException ignored){
                ignored.printStackTrace();//TODO delete before submitting
            }
            this.connectionsDataBase.remove(connectionId);

        }//end of if
    }
}

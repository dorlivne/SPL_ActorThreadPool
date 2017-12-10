package bgu.spl.a2;

import sun.misc.Queue;

public class ActorQueue<T> extends Queue{
private String _Actorid;
private PrivateState _PrivateState;
private boolean _Occupied;

public ActorQueue(String id , PrivateState x){
    super();//builds a queue
    this._Actorid = id;
    this._PrivateState = x;
    _Occupied = false;

}

    public String get_Actorid() {
        return _Actorid;
    }

    public PrivateState get_PrivateState() {
        return _PrivateState;
    }

    public boolean is_Occupied() {
        return _Occupied;
    }

    /**
     * @param _Occupied -> Serves as a sign to check if this queue is locked by another thread
     *                     the reason is to avoid threads to be waiting on queue instead of
     *                     Searching for an available queue to Work on.
     */
    public void set_Occupied(boolean _Occupied) {
        this._Occupied = _Occupied;
    }
}

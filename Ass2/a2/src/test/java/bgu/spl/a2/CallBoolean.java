package bgu.spl.a2;

public class CallBoolean {
    private boolean Called = false;

    public void GotCalled(){
        Called = true;
}
    public boolean getBool(){
        return Called;
    }
}

package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.HashMap;
import java.util.List;

public class CheckRequirments extends Action {
    private Computer _comp;
    private List<String> _Conditions;

    public CheckRequirments(List<String> Conditions , Computer comp){
        this._Conditions = Conditions;
        this._comp = comp;
    }
    @Override
    protected void start() {
        StudentPrivateState student = ((StudentPrivateState)this.ActorState);
        student.setSignature(_comp.checkAndSign(_Conditions,student.getGrades()));
        this.complete(student.getSignature() == _comp.getSuccessSig());//true if passed
    }
}

package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class UnregisterConfirmation extends Action {

    private String StudentID;

    public  UnregisterConfirmation(String studentID){
        this.StudentID = studentID;
    }

    @Override
    protected void start() {
        ((CoursePrivateState)this.ActorState).RemoveStudent(this.StudentID);
        this.getResult().resolve(true);//added successfully
    }
}

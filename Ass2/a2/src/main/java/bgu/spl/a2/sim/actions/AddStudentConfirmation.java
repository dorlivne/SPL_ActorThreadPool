package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

public class AddStudentConfirmation extends Action{

    private String studentID;

    public AddStudentConfirmation (String studentID){
        this.studentID = studentID;
    }

    @Override
    protected void start() {
        ((DepartmentPrivateState)this.ActorState).AddStudentToDepartment(this.studentID);
        this.getResult().resolve(true);//added successfully

    }
}

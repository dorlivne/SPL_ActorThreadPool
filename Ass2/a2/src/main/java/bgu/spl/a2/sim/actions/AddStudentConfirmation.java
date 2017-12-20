package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

public class AddStudentConfirmation extends Action{

    private String depName;
    private String studentID;

    public AddStudentConfirmation (String depName, String studentID){

        this.depName = depName;
        this.studentID = studentID;
        this.setActionName("Add Student Confirmation");
    }

    @Override
    protected void start() {
        ((DepartmentPrivateState)this.pool.getActors().get(this.depName)).AddStudentToDepartment(this.studentID);
        this.getResult().resolve(true);//added successfully

    }
}

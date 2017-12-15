package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

public class CloseCourseConfirmation extends Action {

    private String courseName;

    public CloseCourseConfirmation(String courseName){
        this.courseName = courseName;
        this.setActionName("Close Course Confirmation");
    }

    @Override
    protected void start() {
        ((DepartmentPrivateState) this.ActorState).RemoveCourseFromDepartment(this.courseName);
        this.getResult().resolve(true);

    }
}

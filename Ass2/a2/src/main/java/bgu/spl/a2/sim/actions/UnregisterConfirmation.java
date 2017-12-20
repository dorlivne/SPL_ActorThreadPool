package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class UnregisterConfirmation extends Action {

    private String courseName;

    public  UnregisterConfirmation(String courseName){

        this.courseName = courseName;
        this.setActionName("Unregister Student Confirmation");
    }

    @Override
    protected void start() {
        ((StudentPrivateState) this.ActorState).removeCourse(courseName);//Removing course to student
        this.getResult().resolve(true);//added successfully
    }
}

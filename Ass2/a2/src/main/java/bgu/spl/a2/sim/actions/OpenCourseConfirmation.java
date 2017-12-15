package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;


public class OpenCourseConfirmation  extends Action<Boolean> {

    private String CourseName;

   public OpenCourseConfirmation(String courseName){

       this.CourseName = courseName;
       this.setActionName("Open Course Confirmation");
    }

    @Override
    protected void start() {
        ((DepartmentPrivateState)this.ActorState).AddCourseToDepartment(this.CourseName);
        this.getResult().resolve(true);//added successfully


    }
}

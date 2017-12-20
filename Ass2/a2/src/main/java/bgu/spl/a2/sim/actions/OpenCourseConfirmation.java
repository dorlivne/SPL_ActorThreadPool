package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

import java.util.Vector;


public class OpenCourseConfirmation  extends Action<Boolean> {

    private String courseName;
    private Integer availableSpots;
    private Vector<String> prequisites;

   public OpenCourseConfirmation(String courseName, Vector<String> preReq, Integer availableSpots){

       this.courseName = courseName;
       this.availableSpots = availableSpots;
       this.prequisites = preReq;
       this.setActionName("Open Course Confirmation");
    }

 /*   @Override
    protected void start() {
        ((DepartmentPrivateState)this.ActorState).AddCourseToDepartment(this.CourseName);
        this.getResult().resolve(true);//added successfully


    }
}*/

    @Override
    protected void start() {
        ((CoursePrivateState)this.ActorState).setPrequisites(this.prequisites);
        ((CoursePrivateState)this.ActorState).setAvailableSpots(this.availableSpots);
        this.getResult().resolve(true);//added successfully


    }
}



package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class RemoveCourseFromStudent extends Action {
    private String CourseToBeRemoved;

   public RemoveCourseFromStudent(String course){
       this.CourseToBeRemoved = course;
       }


    @Override
    protected void start() {
        ((StudentPrivateState)this.ActorState).getGrades().remove(CourseToBeRemoved);
        this.complete(true);
    }
}

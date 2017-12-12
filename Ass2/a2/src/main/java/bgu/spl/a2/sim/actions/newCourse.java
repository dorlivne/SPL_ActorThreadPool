package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import java.util.List;

public class newCourse extends Action {

    private String courseName;
    private CoursePrivateState state;




    void newCourse(String courseName, int space, List<String> PreRequisits){
        this.courseName = courseName;
        this.state.setPrequisites(PreRequisits);


    }

    @Override
    protected void start() {
       System.out.println("opening course");


    }
}

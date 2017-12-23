package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

import java.util.ArrayList;
import java.util.List;

public class AddSpaces extends Action {

    private int spaces;
    private String courseName;

    public AddSpaces(String courseName, int spaces){
        this.spaces = spaces;
        this. courseName = courseName;
        this.setActionName("Add Spaces");
    }

    @Override
    protected void start() {
        System.out.println("Adding Spaces");
        this.ActorState.addRecord(getActionName());
        if(((CoursePrivateState)this.ActorState).getAvailableSpots() != -1)//course is still open
        {
            ((CoursePrivateState) this.ActorState).setAvailableSpots(this.spaces);
            System.out.println(this.spaces + " Spaces added to " + this.courseName + " course");
        } else{
            System.out.println("The course is close no spaces added");
        }
        this.getResult().resolve(true);//added successfully
    }
}

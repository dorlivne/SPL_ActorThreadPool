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
    }

    @Override
    protected void start() {
        System.out.println("Adding Spaces");
        ((CoursePrivateState)this.ActorState).setAvailableSpots(this.spaces);
        this.getResult().resolve(true);//added successfully
    }
}

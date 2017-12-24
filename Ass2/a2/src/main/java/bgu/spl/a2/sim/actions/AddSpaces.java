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
        this.ActorState.addRecord(getActionName());
        if(((CoursePrivateState)this.ActorState).getAvailableSpots() != -1)//course is still open
        {
            ((CoursePrivateState) this.ActorState).AddSpaces(this.spaces);
        } else{
        }
        this.getResult().resolve(true);//added successfully
    }
}

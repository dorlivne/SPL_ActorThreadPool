package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class AddSpacesConfirmation extends Action {

    private int spacesToAdd;

    public AddSpacesConfirmation(int spaces){
        this.spacesToAdd = spaces;
    }

    @Override
    protected void start() {
        ((CoursePrivateState)this.ActorState).setAvailableSpots(spacesToAdd);
        this.getResult().resolve(true);//added successfully
    }
}

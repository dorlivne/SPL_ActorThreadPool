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
        List<Action<Boolean>> actions = new ArrayList<>();
        Action<Boolean> AddSpacesConfirmation = new AddSpacesConfirmation(this.spaces);
        actions.add(AddSpacesConfirmation);
        sendMessage(AddSpacesConfirmation, this.courseName, new CoursePrivateState());
        then(actions,()->{
            Boolean result = actions.get(0).getResult().get();//TODO why at place 0? if there are some action which some are good and some are bad
            if(result == true) {
                complete(true);
                System.out.println(this.spaces + " spcaes to" + this.courseName + " added");
            }
            else{
                complete(false);
                System.out.println("No spaces to " + this.courseName + " added");
            }
        });
    }
}

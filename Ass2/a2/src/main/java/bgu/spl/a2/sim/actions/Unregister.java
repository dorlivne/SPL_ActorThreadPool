package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.List;

public class Unregister extends Action {

    private String studentID;
    private String courseName;

    public Unregister(String studentID, String courseName){
        this.studentID = studentID;
        this.courseName = courseName;
        this.setActionName("Unregister");
    }

    @Override
    protected void start() {
        boolean isRegistered = ((CoursePrivateState) this.ActorState).getRegStudents().contains(this.studentID);
        if (!isRegistered) {
            pool.submit(this,this.courseName,this.ActorState);//try again later
        }
        else {
            List<Action<Boolean>> actions = new ArrayList<>();
            Action<Boolean> UnregisterConfirmation = new UnregisterConfirmation(this.courseName);
            actions.add(UnregisterConfirmation);
            sendMessage(UnregisterConfirmation, this.studentID, new StudentPrivateState());
            then(actions, () -> {
                Boolean result = actions.get(0).getResult().get();
                this.ActorState.addRecord(getActionName());
                if (result == true) {
                    ((CoursePrivateState)this.ActorState).RemoveStudent(this.studentID);
                    complete(true);
                } else {
                    complete(false);
                }
            });
        }
    }
}

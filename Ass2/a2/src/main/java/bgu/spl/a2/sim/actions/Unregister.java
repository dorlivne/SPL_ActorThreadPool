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
            this.ActorState.addRecord(getActionName());
            complete(true);
        }
        else {
            System.out.println("Unregistering Student: " + this.studentID);
            List<Action<Boolean>> actions = new ArrayList<>();
            Action<Boolean> UnregisterConfirmation = new UnregisterConfirmation(this.courseName);
            actions.add(UnregisterConfirmation);
            sendMessage(UnregisterConfirmation, this.studentID, new StudentPrivateState());
            then(actions, () -> {
                Boolean result = actions.get(0).getResult().get();
                if (result == true) {
                    this.ActorState.addRecord(getActionName());
                    ((CoursePrivateState)this.ActorState).RemoveStudent(this.studentID);
                    complete(true);
                    System.out.println("student " + this.studentID + " removed from " + this.courseName + " course");
                } else {
                    complete(false);
                    System.out.println("student " + this.studentID + " wasn't removed from " + this.courseName + " course");
                }
            });
        }
    }
}

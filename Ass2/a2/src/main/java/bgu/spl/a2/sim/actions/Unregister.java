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
        System.out.println("Unregistering Student: " + this.studentID);
        List<Action<Boolean>> actions = new ArrayList<>();
        Action<Boolean> UnregisterConfirmation = new UnregisterConfirmation(this.studentID);
        actions.add(UnregisterConfirmation);
        sendMessage(UnregisterConfirmation,this.courseName, new CoursePrivateState());
        then(actions,()->{
            Boolean result = actions.get(0).getResult().get();
            if(result == true) {
                ((StudentPrivateState)this.ActorState).removeCourse(courseName);//Removing course to student
                this.ActorState.addRecord(getActionName());
                complete(true);
                System.out.println("student" + this.studentID + " removed from " + this.courseName + "course");
            }
            else{
                complete(false);
                System.out.println("student" + this.studentID + "wasn't removed from " + this.courseName + "course");
            }
        });
    }
}

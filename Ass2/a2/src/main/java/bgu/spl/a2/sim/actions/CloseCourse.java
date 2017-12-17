package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.List;

public class CloseCourse extends Action{

    private String CourseName;
    private String DepartmentName;

    public CloseCourse(String DepartmentName, String CourseName){
        this.CourseName = CourseName;
        this.DepartmentName = DepartmentName;
        this.setActionName("Close Course");
    }

    @Override
    protected void start() {
        System.out.println("Closing course " + this.CourseName);
        List<Action<Boolean>> actions = new ArrayList<>();
        Action<Boolean> CloseCourseConfirmation = new CloseCourseConfirmation(this.CourseName);
        actions.add(CloseCourseConfirmation);
        sendMessage(CloseCourseConfirmation, this.DepartmentName, new DepartmentPrivateState());
        then(actions,()->{
            Boolean result = actions.get(0).getResult().get();
            if(result == true) {
                {
                    for (String RegStudent : ((CoursePrivateState) this.ActorState).getRegStudents()) {
                        ((StudentPrivateState) this.pool.getPrivateStates(RegStudent)).removeCourse(this.CourseName);
                    }
                    ((CoursePrivateState) this.ActorState).getRegStudents().clear();
                    ((CoursePrivateState) this.ActorState).setRegistered(0);
                    ((CoursePrivateState) this.ActorState).setAvailableSpots(-1);
                    complete(true);
                    this.ActorState.addRecord(getActionName());
                    System.out.println("The course: " + this.CourseName + " is close");
                }
            }
            else{
                complete(false);
                System.out.println("The course " + this.CourseName + " isn't colse");
            }
        });
    }
}

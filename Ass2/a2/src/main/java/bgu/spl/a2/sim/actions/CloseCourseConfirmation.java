package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.List;

public class CloseCourseConfirmation extends Action {

    private String courseName;

    public CloseCourseConfirmation(String courseName){
        this.courseName = courseName;
        this.setActionName("Close Course Confirmation");
    }

    @Override
    protected void start() {
        // for (String RegStudent : ((CoursePrivateState) this.ActorState).getRegStudents()) {
        // ((StudentPrivateState) this.pool.getPrivateStates(RegStudent)).removeCourse(this.courseName);
        //}
        List<Action<Boolean>> actions = new ArrayList<>();
        for (String RegStudent : ((CoursePrivateState) this.ActorState).getRegStudents()) {
            RemoveCourseFromStudent Remover = new RemoveCourseFromStudent(courseName);
            actions.add(Remover);
            this.sendMessage(Remover, RegStudent, new StudentPrivateState());
        }
        then(actions, () -> {
            ((CoursePrivateState) this.ActorState).getRegStudents().clear();
            ((CoursePrivateState) this.ActorState).setRegistered(0);
            ((CoursePrivateState) this.ActorState).setAvailableSpots(-1);
            this.getResult().resolve(true);

        });
    }
}

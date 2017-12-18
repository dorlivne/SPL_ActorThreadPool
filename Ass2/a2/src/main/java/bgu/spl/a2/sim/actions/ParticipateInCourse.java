package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.List;

public class ParticipateInCourse extends Action {

    private String studentID;
    private String courseName;
    private int grade;

    public ParticipateInCourse(String studentID, String courseName, int grade){
        this.studentID = studentID;
        this.courseName = courseName;
        this.grade = grade;
        this.setActionName("Participate In Course");
    }

    @Override
    protected void start() {
        System.out.println("Registering Student - no Pref");
        List<Action<Boolean>> actions = new ArrayList<>();
        Action<Boolean> ParticipateInCourseConfirmation = new ParticipateInCourseConfirmation(this.studentID,this.courseName,((StudentPrivateState)this.ActorState).getGrades());
        actions.add(ParticipateInCourseConfirmation);
        sendMessage(ParticipateInCourseConfirmation,this.courseName, new CoursePrivateState());
        then(actions,()->{
            Boolean result = actions.get(0).getResult().get();
            if(result == true) {
                ((StudentPrivateState)this.ActorState).addCourseAndGrade(courseName,grade);//Adding course to student
                this.ActorState.addRecord(getActionName());
                complete(true);
                System.out.println("student " + this.studentID + " added to " + this.courseName + " course");
            }
            else{
                complete(false);
                System.out.println("student " + this.studentID + " wasn't added to " + this.courseName + " course");
            }
        });
    }
}

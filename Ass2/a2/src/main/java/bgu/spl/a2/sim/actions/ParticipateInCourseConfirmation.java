package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.HashMap;
import java.util.List;

public class ParticipateInCourseConfirmation extends Action {

    private String studentID;
    private String courseName;
    private int grade;


    public ParticipateInCourseConfirmation(String studentID, String courseName, int grade) {
        this.courseName = courseName;
        this.grade = grade;
        this.studentID = studentID;
        this.setActionName("Participate In Course Confirmation");
    }

    @Override
    protected void start() {//TODO Sync?-if only one thread is here it's ok. check with LivneG
        ((StudentPrivateState) this.ActorState).addCourseAndGrade(courseName, grade);//Adding course to student
        this.getResult().resolve(true);//added successfully
    }
}



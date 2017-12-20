package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.HashMap;
import java.util.List;
import java.util.HashMap;

public class RegStudentConfirmation extends Action {

    private String studentID;
    private String courseName;
    private Integer grade;


    public RegStudentConfirmation(String studentID, String courseName, Integer grade){
        this.courseName = courseName;
        this.grade = grade;
        this.studentID =studentID;
        this.setActionName("Register Student Confirmation");
    }

    @Override
    protected void start() {//TODO Sync?-if only one thread is here it's ok. check with LivneG
        ((StudentPrivateState) this.pool.getActors().get(studentID)).addCourseAndGrade(this.courseName,this.grade);//TODO the course and it's grade to the student
            this.getResult().resolve(true);//added successfully

    }
}

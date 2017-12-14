package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

import java.util.HashMap;
import java.util.List;

public class ParticipateInCourseConfirmation extends Action{

    private String studentID;
    private String courseName;
    private HashMap<String,Integer> CorsesAndGrades;


    public ParticipateInCourseConfirmation(String studentID, String courseName, HashMap<String,Integer> CorsesAndGrades){
        this.courseName = courseName;
        this.CorsesAndGrades = CorsesAndGrades;
        this.studentID =studentID;
    }

    @Override
    protected void start() {//TODO Sync?-if only one thread is here it's ok. check with LivneG
        if(((CoursePrivateState)this.ActorState).HasReqCourses(((CoursePrivateState)this.ActorState).getPrequisites(), this.CorsesAndGrades) && ((CoursePrivateState)this.ActorState).getAvailableSpots() >= 1 ) {
            ((CoursePrivateState)this.ActorState).updateParametrs(1, this.studentID);//reg + 1
            ((CoursePrivateState)this.ActorState).setRegStudents(this.studentID);//add studentID to courseReg
            this.getResult().resolve(true);//added successfully
        }
        else
            this.getResult().resolve(false);

    }
}



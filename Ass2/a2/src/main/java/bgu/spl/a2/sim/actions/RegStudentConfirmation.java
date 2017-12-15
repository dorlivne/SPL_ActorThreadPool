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
    private List<String> prequisites;
    private HashMap<String,Integer> CorsesAndGrades;


    public RegStudentConfirmation(String studentID, String courseName, List<String> prequisites, HashMap<String,Integer> CorsesAndGrades){
        this.prequisites = prequisites;
        this.courseName = courseName;
        this.CorsesAndGrades = CorsesAndGrades;
        this.studentID =studentID;
        this.setActionName("Register Student Confirmation");
    }

    @Override
    protected void start() {//TODO Sync?-if only one thread is here it's ok. check with LivneG
        if(((CoursePrivateState)this.ActorState).HasReqCourses(prequisites, this.CorsesAndGrades) && ((CoursePrivateState)this.ActorState).getAvailableSpots() >= 1 ) {
            ((CoursePrivateState)this.ActorState).updateParametrs(1,this.studentID);//reg + 1
            ((CoursePrivateState)this.ActorState).setRegStudents(this.studentID);//add studentID to courseReg
            this.getResult().resolve(true);//added successfully
        }
        else
            this.getResult().resolve(false);

    }
}

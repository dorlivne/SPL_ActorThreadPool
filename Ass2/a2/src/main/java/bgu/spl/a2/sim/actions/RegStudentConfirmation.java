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
    private int gradeOfAddedCourse;
    private HashMap<String,Integer> Grades;


    public RegStudentConfirmation(String studentID, String courseName, List<String> prequisites, int gradeOfAddedCourse, HashMap<String,Integer> Grades){
        this.prequisites = prequisites;
        this.courseName = courseName;
        this.gradeOfAddedCourse = gradeOfAddedCourse;
        this.Grades = Grades;
    }

    @Override
    protected void start() {//TODO Sync?
        if(((CoursePrivateState)this.ActorState).HasReqCourses(prequisites, this.Grades) && ((CoursePrivateState)this.ActorState).getAvailableSpots() > 1 ) {
            ((CoursePrivateState)this.ActorState).setAvailableSpots(((CoursePrivateState)this.ActorState).getAvailableSpots() - 1);//spaces - 1
            ((CoursePrivateState)this.ActorState).setRegistered();//reg + 1
            ((CoursePrivateState)this.ActorState).setRegStudents(this.studentID);//add studentID to courseReg
            this.getResult().resolve(true);//added successfully
        }
        else
            this.getResult().resolve(false);

    }
}

package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class RegisterStudent extends Action {

    private String studentID;
    private List<Integer> Grades;
    private List<String> PreferenceStr;
    private String currentPref;
    private List<String> preReq;
    private HashMap<String,Integer> studentGrades;
    private int availibalespots;
    private int currentGrade;

    public RegisterStudent(String studentID, List<String> PreferenceStr, List<Integer> grades) {
        this.studentID = studentID;
        this.Grades = grades;
        this.PreferenceStr = PreferenceStr;
        this.currentPref = this.PreferenceStr.get(0);
        this.setActionName("Register Student");
    }

    @Override
    protected void start() {
        boolean RegisteredAlready = ((CoursePrivateState) this.pool.getActors().get(currentPref)).getRegStudents().contains(this.studentID);
        this.preReq = ((CoursePrivateState) this.pool.getActors().get(currentPref)).getPrequisites();
        this.studentGrades = ((StudentPrivateState) this.pool.getActors().get(this.studentID)).getGrades();
        this.availibalespots = ((CoursePrivateState) this.pool.getActors().get(this.currentPref)).getAvailableSpots();

        if (!RegisteredAlready) {
            if (((CoursePrivateState) this.ActorState).HasReqCourses(this.preReq, this.studentGrades) && availibalespots > 0) {
                ((CoursePrivateState) this.ActorState).updateParametrs(1, this.studentID);//reg + 1
                ((CoursePrivateState) this.ActorState).setRegStudents(this.studentID);//add studentID to courseReg
                this.currentGrade = this.Grades.get(this.PreferenceStr.indexOf(currentPref));

                List<Action<Boolean>> actions = new ArrayList<>();
                Action<Boolean> RegStudentConfirmation = new RegStudentConfirmation(this.studentID, this.currentPref, this.currentGrade);
                actions.add(RegStudentConfirmation);
                sendMessage(RegStudentConfirmation, this.studentID, new StudentPrivateState());

                then(actions, () -> {
                    Boolean result = actions.get(0).getResult().get();
                    if (result == true) {
                        complete(true);
                        this.ActorState.addRecord(getActionName());
                    } else {//registered to none
                        complete(false);
                        this.ActorState.addRecord(getActionName());
                    }
                });
            } else if (PreferenceStr.size() != 1){
                this.NextPref();
            } else{
                complete(true);
            }
        } else if (PreferenceStr.size() != 1){
            this.NextPref();
        } else{
            this.ActorState.addRecord(getActionName());
            complete(true);
        }
    }


    private void NextPref(){
        this.PreferenceStr.remove(0);
        this.currentPref = this.PreferenceStr.get(0);
        this.start();
    }
}


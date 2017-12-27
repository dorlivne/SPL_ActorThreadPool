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

    public RegisterStudent(String studentID, List<String> PreferenceStr, List<Integer> grades) {
        this.studentID = studentID;
        Grades = grades;
        this.PreferenceStr = PreferenceStr;
        if(this.PreferenceStr.size() > 0){
            this.currentPref = this.PreferenceStr.get(0);
        }
        else
            this.currentPref = null;

        this.setActionName("Register With Preference");
    }

    @Override
    protected void start() {
        List<Action<Boolean>> actions = new ArrayList<>();
        Action<Boolean> RegStudentConfirmation = new RegStudentConfirmation(this.studentID, PreferenceStr.get(0), ((StudentPrivateState) this.pool.getActors().get(studentID)).getGrades());
        actions.add(RegStudentConfirmation);
        sendMessage(RegStudentConfirmation, this.currentPref, new CoursePrivateState());

        then(actions, () -> {
            Boolean result = actions.get(0).getResult().get();
            if (result == true) {
                this.ActorState.addRecord(getActionName());
                ((StudentPrivateState) this.pool.getActors().get(studentID)).addCourseAndGrade(currentPref, Grades.get(PreferenceStr.indexOf(currentPref)));//TODO the course and it's grade to the student
                complete(true);
            } else if (PreferenceStr.size() != 1) {//there are more prefs to check
                this.NextPref();
            } else{//registered to none
                complete(false);
                this.ActorState.addRecord(getActionName());
            }
        });
    }

    private void NextPref(){
        this.PreferenceStr.remove(0);
        this.currentPref = this.PreferenceStr.get(0);
        this.start();
    }
}
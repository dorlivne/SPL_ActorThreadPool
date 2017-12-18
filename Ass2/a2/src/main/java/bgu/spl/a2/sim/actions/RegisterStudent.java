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

        this.setActionName("Register Student");
    }

    @Override
    protected void start() {
        System.out.println("Registering Student - With Pref");
        List<Action<Boolean>> actions = new ArrayList<>();
            Action<Boolean> RegStudentConfirmation = new RegStudentConfirmation(this.studentID, PreferenceStr.get(0), ((StudentPrivateState) this.pool.getActors().get(studentID)).getGrades());
            actions.add(RegStudentConfirmation);
        sendMessage(RegStudentConfirmation, this.currentPref, new CoursePrivateState());

        then(actions, () -> {
                Boolean result = actions.get(0).getResult().get();
                if (result == true) {
                    ((StudentPrivateState) this.pool.getActors().get(studentID)).addCourseAndGrade(currentPref, Grades.get(PreferenceStr.indexOf(currentPref)));//TODO the course and it's grade to the student
                    this.ActorState.addRecord(getActionName());
                    complete(true);
                    System.out.println("Student: " + this.studentID + " registered to course " + this.currentPref);
                } else if (PreferenceStr.size() != 1) {//ToDO check if it is ok to block it till last option
                    this.NextPref();
                } else{//registered to none
                    complete(false);
                    System.out.println("Student " + this.studentID + " not registered for none of his prefs");
                }
        });
    }

    private void NextPref(){
        this.PreferenceStr.remove(0);
        this.currentPref = this.PreferenceStr.get(0);
        this.start();
    }
}



//TODO - old attempt - pref is a list of private sates and "then" is in the loop

  /*     int index = 0;
        for (CoursePrivateState pref : Preferences) {//add all preferences to the list of callbacks
            if (!this.getResult().isResolved()) {
                Action<Boolean> RegStudentConfirmation = new RegStudentConfirmation(this.studentID,this.Preferences.get(index).toString() ,this.Preferences.get(index).getPrequisites(), this.privateState.getGrades());
                actions.add(RegStudentConfirmation);
                sendMessage(RegStudentConfirmation, this.Preferences.get(index).toString(), new CoursePrivateState());
                then(actions, () -> {
                    Boolean result = actions.get(Preferences.indexOf(pref)).getResult().get();
                    if (result == true) {
                        this.privateState.addCourseAndGrade(this.Preferences.get(Preferences.indexOf(pref)).toString(),Grades.get(Preferences.indexOf(pref)));//TODO the course and it's grade to the student
                        this.ActorState.addRecord(getActionName());
                        complete(true);
                        System.out.println("Student:" + this.studentID + "registered");
                    } else if (Preferences.indexOf(pref) == Preferences.size()) {//ToDO check if it is ok to block it till last option
                        complete(false);
                        System.out.println("Student" + this.studentID + "not registered");
                    }

                });
                index++;
            }
            else// Hopefully will not add another action
                break;
        }
    }
}*/

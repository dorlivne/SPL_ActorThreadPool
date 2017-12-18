package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterStudent extends Action {

    private String studentID;
    private List<Integer> Grades;
    private List<String> PreferenceStr;

    public RegisterStudent(String studentID, List<String> PreferenceStr, List<Integer> grades) {
        this.studentID = studentID;
        Grades = grades;
        this.PreferenceStr = PreferenceStr;
        this.setActionName("Register Student");
    }

    @Override
    protected void start() {
        System.out.println("Registering Student - With Pref");
        List<Action<Boolean>> actions = new ArrayList<>();
        for (String pref : PreferenceStr) {
            Action<Boolean> RegStudentConfirmation = new RegStudentConfirmation(this.studentID, pref, ((CoursePrivateState) this.pool.getActors().get(pref)).getPrequisites(), ((StudentPrivateState) this.pool.getActors().get(studentID)).getGrades());
            actions.add(RegStudentConfirmation);
            sendMessage(RegStudentConfirmation, pref, new CoursePrivateState());
        }

        then(actions, () -> {
            for (String pref : PreferenceStr) {
                Boolean result = actions.get(PreferenceStr.indexOf(pref)).getResult().get();
                if (result == true) {
                    ((StudentPrivateState) this.pool.getActors().get(studentID)).addCourseAndGrade(pref, Grades.get(PreferenceStr.indexOf(pref)));//TODO the course and it's grade to the student
                    this.ActorState.addRecord(getActionName());
                    complete(true);
                    System.out.println("Student:" + this.studentID + "registered");
                    break;
                } else if (PreferenceStr.indexOf(pref) == PreferenceStr.size()) {//ToDO check if it is ok to block it till last option
                    complete(false);
                    System.out.println("Student" + this.studentID + "not registered for none of his prefs");
                }
            }
        });
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

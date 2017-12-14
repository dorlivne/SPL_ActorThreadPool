package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterStudent extends Action {

    private String studentID;
    private List<CoursePrivateState> Preferences;//for perquisites
    private StudentPrivateState privateState;
    private List<Integer> Grades;

    public RegisterStudent(String studentID, List<CoursePrivateState> Preferences,List<Integer> grades) {
        this.studentID = studentID;
        this.Preferences = Preferences;
        this.privateState = (StudentPrivateState) this.ActorState;//Maybe create a for loop to search for relevant actor and its privateState
        Grades = grades;
        this.setActionName("Register Student");
    }

    @Override
    protected void start() {
        System.out.println("Registering Student");
        int index = 0;
        List<Action<Boolean>> actions = new ArrayList<>();
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
}

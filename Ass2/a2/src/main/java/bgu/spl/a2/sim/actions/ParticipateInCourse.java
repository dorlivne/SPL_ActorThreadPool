package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.*;

public class ParticipateInCourse extends Action {

    private String studentID;
    private String courseName;
    private int grade;
    private List<String> preReq;
    private HashMap<String,Integer> studentGrades;
    private int availibalespots;

    public ParticipateInCourse(String studentID, String courseName, int grade){
        this.studentID = studentID;
        this.courseName = courseName;
        this.grade = grade;
        this.setActionName("Participate In Course " + this.studentID +" " + grade);
    }

  /*  @Override
    protected void start() {
        System.out.println("Registering Student - no Pref -" + this.studentID);
        List<Action<Boolean>> actions = new ArrayList<>();
        Action<Boolean> ParticipateInCourseConfirmation = new ParticipateInCourseConfirmation(this.studentID,this.courseName,((StudentPrivateState)this.ActorState).getGrades());
        actions.add(ParticipateInCourseConfirmation);
        sendMessage(ParticipateInCourseConfirmation,this.courseName, this.pool.getPrivateStates(this.courseName));
        then(actions,()->{
            Boolean result = actions.get(0).getResult().get();
            if(result == true) {
                ((StudentPrivateState)this.ActorState).addCourseAndGrade(courseName,grade);//Adding course to student
                this.ActorState.addRecord(getActionName());
                complete(true);
                System.out.println("student " + this.studentID + " added to " + this.courseName + " course");
            }
            else{
                complete(false);
                System.out.println("student " + this.studentID + " wasn't added to " + this.courseName + " course");
            }
        });
    }
}*/


    @Override
    protected void start() {
        System.out.println("Registering Student - no Pref -" + this.studentID);

        boolean RegisteredAlready = ((CoursePrivateState) this.ActorState).getRegStudents().contains(this.studentID);
        this.preReq = ((CoursePrivateState) this.ActorState).getPrequisites();
        this.studentGrades = ((StudentPrivateState) this.pool.getActors().get(this.studentID)).getGrades();
        this.availibalespots = ((CoursePrivateState) this.pool.getActors().get(this.courseName)).getAvailableSpots();

        if (!RegisteredAlready) {
            if (((CoursePrivateState) this.ActorState).HasReqCourses(this.preReq, this.studentGrades) && availibalespots > 0) {
                ((CoursePrivateState) this.ActorState).updateParametrs(1, this.studentID);//reg + 1
                ((CoursePrivateState) this.ActorState).setRegStudents(this.studentID);//add studentID to courseReg
                List<Action<Boolean>> actions = new ArrayList<>();
                Action<Boolean> ParticipateInCourseConfirmation = new ParticipateInCourseConfirmation(this.studentID, this.courseName, this.grade);
                actions.add(ParticipateInCourseConfirmation);
                sendMessage(ParticipateInCourseConfirmation, this.studentID, new StudentPrivateState());
                then(actions, () -> {
                    Boolean result = actions.get(0).getResult().get();
                    if (result == true) {
                        this.ActorState.addRecord(getActionName());
                        complete(true);
                        System.out.println("student " + this.studentID + " added to " + this.courseName + " course");
                    } else {
                        complete(false);
                        System.out.println("student " + this.studentID + " wasn't added to " + this.courseName + " course");
                    }
                });
            } else {
                System.out.println("student " + this.studentID + " wasn't added to " + this.courseName + " course");
                this.ActorState.addRecord(getActionName());
                complete(true);
            }
        } else {
            pool.submit(this,this.courseName,this.ActorState);//try again
        }
    }
}
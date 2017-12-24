package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class OpenANewCourse extends Action<Boolean> {

    private String courseName;
    private String departmentName;
    private Integer availableSpots;
    private Vector<String> prequisites;


    public OpenANewCourse(String courseName, Integer spaces, Vector<String> PreRequisits, String DepartmentName){//TODO - if complete = false check if the course was added
        this.courseName = courseName;
        this.departmentName = DepartmentName;
        this.availableSpots = spaces;
        this.prequisites = PreRequisits;
        this.setActionName("Open A New Course");
    }

    @Override
    protected void start() {
        List<Action<Boolean>> actions = new ArrayList<>();
        Action<Boolean> OpenCourseConfirmation = new OpenCourseConfirmation(this.courseName,this.prequisites,this.availableSpots);
        actions.add(OpenCourseConfirmation);
        sendMessage(OpenCourseConfirmation, this.courseName, new CoursePrivateState());
        then(actions,()->{
            Boolean result = actions.get(0).getResult().get();
            this.ActorState.addRecord(getActionName());
            if(result == true) {
                complete(true);
                ((DepartmentPrivateState)this.ActorState).AddCourseToDepartment(this.courseName);
            }
            else{
                complete(false);
            }
        });

    }
}

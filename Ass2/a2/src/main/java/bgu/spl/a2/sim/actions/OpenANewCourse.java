package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.List;

public class OpenANewCourse extends Action<Boolean> {

    private String courseName;
    private CoursePrivateState courseState;
    private String departmentName;




    void newCourse(String courseName, Integer space, List<String> PreRequisits, String DepartmentName){
        this.courseName = courseName;
        this.courseState.setPrequisites(PreRequisits);
        this.courseState.setAvailableSpots(space);
        this.departmentName = DepartmentName;
    }

    @Override
    protected void start() {
        System.out.println("Opening course");
        List<Action<Boolean>> actions = new ArrayList<>();
        Action<Boolean> OpenCourseConfirmation = new OpenCourseConfirmation(this.courseName);
        actions.add(OpenCourseConfirmation);
        sendMessage(OpenCourseConfirmation,this.departmentName, new DepartmentPrivateState());
        then(actions,()->{
            Boolean result = actions.get(0).getResult().get();
            if(result == true) {
                complete(true);
                System.out.println("course" + this.courseName + "opened");
            }
            else{
                complete(false);
                System.out.println("course" + this.courseName + "not opened");
            }
        });

    }
}

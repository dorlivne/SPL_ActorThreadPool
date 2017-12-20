package bgu.spl.a2.sim;


import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.actions.*;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class JsonFunctions {

    public static HashMap<String,Computer> GetComputers(JsonArray ComputersCollection){
        HashMap<String,Computer> ComputerHashMap = new HashMap<>();
        for(int i = 0;i < ComputersCollection.size();i++){
            JsonObject Comp = ComputersCollection.get(i).getAsJsonObject();
            String ComputerName = Comp.get("Type").getAsString();
            long SigSuccess = Comp.get("Sig Success").getAsLong();
            long SigFail = Comp.get("Sig  Fail").getAsLong();
            Computer x = new Computer(ComputerName,SigFail,SigSuccess);
            ComputerHashMap.put(ComputerName,x);
        }
        return ComputerHashMap;

    }
    public static LinkedList<Action> GetActions(JsonArray ActionsCollection, ActorThreadPool pool, Warehouse warehouse) {
        String ActorName;//first actor before submit
        String  CoActorName;//Actor that we need to update
        int HelperPara;//if extra parameters are needed (such as grade in the course)
        int Space;

        LinkedList<Action> ActionList = new LinkedList<>();
        for (int i = 0; i < ActionsCollection.size(); i++) {
            JsonObject action = ActionsCollection.get(i).getAsJsonObject();
            String ActionName = action.get("Action").getAsString();//holds the string name

            switch (ActionName){
                case "Open Course":
                    ActorName = action.get("Course").getAsString();//here the main actor is course
                    CoActorName = action.get("Department").getAsString();
                    Vector<String> PreReq = new Vector<>();
                    JsonArray JsonPreReq = action.get("Prerequisites").getAsJsonArray();
                    for (JsonElement jsonElement : JsonPreReq) {
                        PreReq.add(jsonElement.getAsString());
                    }
                    Space = action.get("Space").getAsInt();
                    OpenANewCourse openNewCourse = new OpenANewCourse(ActorName,Space,PreReq,CoActorName);
                    ActionList.add(openNewCourse);
                    pool.submit(openNewCourse,ActorName,new CoursePrivateState());
                    break;
                case "Add Student":
                    ActorName = action.get("Student").getAsString();
                    CoActorName = action.get("Department").getAsString();
                    AddStudent AddStudent = new AddStudent(CoActorName,ActorName);
                    ActionList.add(AddStudent);
                    pool.submit(AddStudent,ActorName,new StudentPrivateState());
                    break;
                case "Participate In Course":
                    ActorName = action.get("Student").getAsString();
                    CoActorName = action.get("Course").getAsString();
                    HelperPara = action.get("Grade").getAsInt();
                    ParticipateInCourse ParticipateInCourse = new ParticipateInCourse(ActorName,CoActorName,HelperPara);
                    ActionList.add(ParticipateInCourse);
                    pool.submit(ParticipateInCourse,ActorName,new StudentPrivateState());
                    break;
                case "Unregister":
                    ActorName = action.get("Student").getAsString();
                    CoActorName = action.get("Course").getAsString();
                    Unregister Unregister = new Unregister(ActorName,CoActorName);
                    ActionList.add(Unregister);
                    pool.submit(Unregister,ActorName,new StudentPrivateState());
                    break;
                case "Administrative Check":
                    ActorName = action.get("Department").getAsString();
                    JsonArray CoActor = action.get("Students").getAsJsonArray();
                    List<String> StudentsID = new LinkedList<>();
                    for (JsonElement jsonElement : CoActor) {//list of students
                        StudentsID.add(jsonElement.getAsString());
                    }
                    JsonPreReq = action.get("Conditions").getAsJsonArray();
                    List<String> PreReqList = new LinkedList<>();
                    for (JsonElement jsonElement : JsonPreReq) {
                        PreReqList.add(jsonElement.getAsString());
                    }
                    String Computer = action.get("Computer").getAsString();
                    CheckAdministrativeObligations CheckAdministrativeObligations = new CheckAdministrativeObligations(StudentsID,PreReqList,Computer,warehouse);
                    ActionList.add(CheckAdministrativeObligations);
                    pool.submit(CheckAdministrativeObligations,ActorName,new DepartmentPrivateState());
                    break;
                case "Add Spaces":
                    ActorName = action.get("Course").getAsString();
                    HelperPara = action.get("Number").getAsInt();
                    AddSpaces AddSpaces = new AddSpaces(ActorName,HelperPara);
                    ActionList.add(AddSpaces);
                    pool.submit(AddSpaces,ActorName,new CoursePrivateState());
                    break;
                case "Register With Preferences":
                    ActorName = action.get("Student").getAsString();//here the main actor is course
                    CoActor = action.get("Preferences").getAsJsonArray();
                    List<String> Pref = new LinkedList<>();
                    for (JsonElement jsonElement : CoActor) {//list of students
                        Pref.add(jsonElement.getAsString());
                    }
                    JsonPreReq = action.get("Grades").getAsJsonArray();//grades!!
                    List<Integer> Grades = new LinkedList<>();
                    for (JsonElement jsonElement : JsonPreReq) {
                        Grades.add(jsonElement.getAsInt());
                    }
                    Space = action.get("Space").getAsInt();
                    RegisterStudent RegisterStudent = new RegisterStudent(ActorName,Pref,Grades);
                    ActionList.add(RegisterStudent);
                    pool.submit(RegisterStudent,ActorName,new StudentPrivateState());
                    break;
                case "Close Course":
                    ActorName = action.get("Course").getAsString();
                    CoActorName = action.get("Department").getAsString();
                    CloseCourse CloseCourse = new CloseCourse(ActorName,CoActorName);
                    ActionList.add(CloseCourse);
                    pool.submit(CloseCourse,ActorName,new CoursePrivateState());
                    break;

                    default:
                        break;
            }


        }
        return ActionList;
    }
}

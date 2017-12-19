package bgu.spl.a2.sim;


import bgu.spl.a2.Action;
import bgu.spl.a2.sim.actions.OpenANewCourse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.LinkedList;
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
    public static LinkedList<Action> GetActions(JsonArray ActionsCollection) {
        LinkedList<Action> ActionList = new LinkedList<>();
        for (int i = 0; i < ActionsCollection.size(); i++) {
            JsonObject action = ActionsCollection.get(i).getAsJsonObject();
            String ActionName = action.get("Action").getAsString();//holds the string name

            switch (ActionName){
                case "Open Course":
                    String ActorName = action.get("Department").getAsString();
                    String  Name = action.get("Course").getAsString();
                    JsonArray JsonPreReq = action.get("Prerequisites").getAsJsonArray();
                    Vector<String> PreReq = new Vector<>();
                    for (JsonElement jsonElement : JsonPreReq) {
                        PreReq.add(jsonElement.getAsString());
                    }
                    int Space = action.get("Space").getAsInt();
                    OpenANewCourse openNewCourse = new OpenANewCourse(Name,Space,PreReq,ActorName);
                    ActionList.add(openNewCourse);
                    break;
                case "Add Student":
                    break;
                case "Participate In Course":
                    break;
                case "Unregister":
                    break;
                case "Administrative Check":
                    break;
                case "Add Spaces":
                    break;
                case "Register With Preferences":
                    break;
                case "Close Course":
                    break;
            }


        }
        return ActionList;
    }
}

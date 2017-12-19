/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.PrivateState;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

	public static ActorThreadPool actorThreadPool;
	private static JsonObject Json;
	private static Warehouse warehouse;
	private static CountDownLatch ActionToComplete1;

	/**
	 * Begin the simulation Should not be called before attachActorThreadPool()
	 */
	public static void start(){
		////////Computer Build////////
		JsonArray Computers = Json.get("Computers").getAsJsonArray();
		HashMap<String,Computer> ComputersCollection = JsonFunctions.GetComputers(Computers);
		warehouse = new Warehouse(ComputersCollection);
		actorThreadPool.start();
		//Finished building a warehouse////////
		//////Start Phase 1/////////
		StartFunc("Phase 1");
		System.out.println("End Of Phase 1");
		////End Of Phase 1/////
		////////Start Phase 2//////////
		StartFunc("Phase 2");
		System.out.println("End Of Phase 2");
		/////////End Phase 2////////
		////////Start Phase 3///////
		StartFunc("Phase 3");
		System.out.println("End Of Phase 3");
		////////End Phase 3///////


	}
	private static void StartFunc(String Phase){
		JsonArray PhaseActions = Json.get(Phase).getAsJsonArray();
		LinkedList<Action> Actions = JsonFunctions.GetActions(PhaseActions,actorThreadPool,warehouse);
		ActionToComplete1 = new CountDownLatch(Actions.size());
		for (Action action : Actions) {
			action.getResult().subscribe(()->ActionToComplete1.countDown());
		}
		try{
			ActionToComplete1.await();
		}catch(InterruptedException e){}
	}

	/**
	 * attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
	 *
	 * @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
	 */
	public static void attachActorThreadPool(ActorThreadPool myActorThreadPool){
		actorThreadPool = myActorThreadPool;
	}

	/**
	 * shut down the simulation
	 * returns list of private states
	 */
	public static HashMap<String,PrivateState> end(){
		try {
			actorThreadPool.shutdown();
		}catch(InterruptedException ignored){}
		HashMap<String,PrivateState> SimulationResult = ((HashMap<String,PrivateState>)actorThreadPool.getActors());
		try {
			FileOutputStream fout = new FileOutputStream("result.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(SimulationResult);
		}catch(FileNotFoundException e){
			System.out.println("File not found");
		}
		catch(IOException e){e.toString();}

		return SimulationResult;
	}

	//TODO CHANGE BACK TO INT!!!!!!
	public static void main(String [] args){
		//String path = args[0];
		String path = "C:\\test.json";
		JsonParser Parser = new JsonParser();
		JsonObject jsondocumnet = null;
		int ThreadNumber;
		try{
			jsondocumnet = Parser.parse(new FileReader(path)).getAsJsonObject();
		}catch(FileNotFoundException e){}
		Json= jsondocumnet;
		//ThreadNumber = jsondocumnet.get("threads").getAsInt();
		ThreadNumber = 1;//TODO - for tests only
		ActorThreadPool pool = new ActorThreadPool(ThreadNumber);
		attachActorThreadPool(pool);
		start();
	}
}

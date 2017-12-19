/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

	/**
	 * Begin the simulation Should not be called before attachActorThreadPool()
	 */
	public static void start(){
		////////Computer Build////////
		JsonArray Computers = Json.get("Computers").getAsJsonArray();
		HashMap<String,Computer> ComputersCollection = JsonFunctions.GetComputers(Computers);
		warehouse = new Warehouse(ComputersCollection);
		//Finished building a warehouse
		//////Start Phase 1/////////
		JsonArray Phase1Actions = Json.get("Phase 1").getAsJsonArray();
		CountDownLatch ActionToComplete = new CountDownLatch(Phase1Actions.size());
		LinkedList<Action> Actions = JsonFunctions.GetActions(Phase1Actions);
		String test = "dasdas";
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
		return ((HashMap<String,PrivateState>)actorThreadPool.getActors());
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
		ThreadNumber = jsondocumnet.get("threads").getAsInt();
		ActorThreadPool pool = new ActorThreadPool(ThreadNumber);
		attachActorThreadPool(pool);
		start();
	}
}

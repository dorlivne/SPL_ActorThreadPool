package bgu.spl.a2.sim;

import bgu.spl.a2.Promise;

import java.util.HashMap;
import java.util.Vector;

/**
 * represents a warehouse that holds a finite amount of computers
 *  and their suspended mutexes.
 *
 */
public class Warehouse {
    private HashMap<String,Computer> Computers;


    /*
        Some Action Call the warehouse in order to claim a certain computer(As noted in the json file) so we search the
        computer here and try to claim it through warehouse ,thats why we need to have a public promise <Computer > function
     */
    public Warehouse(HashMap<String,Computer> Computers) {
        this.Computers = Computers;
    }

    public Promise<Computer> GetComputer(String ComputerName){
        return Computers.get(ComputerName).Key.down();
        }
    }




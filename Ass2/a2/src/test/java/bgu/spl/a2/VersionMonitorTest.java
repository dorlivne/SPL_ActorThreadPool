package bgu.spl.a2;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

public class VersionMonitorTest extends TestCase {


    public void testGetVersion() {
        VersionMonitor A = new VersionMonitor();
        try {
            int i = A.getVersion();
            assertEquals("GetVersion  Test 1 failed ",0,i );
        }catch( AssertionFailedError e){
            System.out.println(e.getMessage());
         }
         Thread t1 = new Thread(()->A.inc());
        Thread t2 = new Thread(()-> A.inc());
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        }catch(InterruptedException ignore){}
        try{
            int i = A.getVersion();
            assertEquals("GetVersion Test 2 failed ",2,i);
        }catch( AssertionFailedError e){
            System.out.println(e.getMessage() +"\n");
        }
    }

    public void testInc(){
        VersionMonitor A = new VersionMonitor();
        int i = A.getVersion();
        A.inc();
        int j = A.getVersion();
        try{
            assertEquals("testInc Test 1 Failed",i+1,j);
        }catch(AssertionFailedError e){
            System.out.println(e.getMessage());
        }

        Thread t1 = new Thread(new Runnable(){
            @Override
            public void run() {
                if(A.getVersion()<=j)
                    A.inc();
            }});
        Thread t2 = new Thread(new Runnable(){
            @Override
            public void run() {
                if(A.getVersion()<=j)
                    A.inc();
            }});
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        }catch(InterruptedException ignore){}
        try{
            assertEquals("testInc Test 2 Failed",j+1,A.getVersion());
        }catch(AssertionFailedError e){
            System.out.println(e.getMessage());
        }
    }

    public void testAwait(){
        VersionMonitor A = new VersionMonitor();
        Thread t1 = new Thread(new Runnable(){
            @Override
            public void run() {
                 try {
                        A.await(0);
                }catch(InterruptedException e){
                        A.inc();
                }
         }});
        t1.start();
        try{
            assertEquals("testAwait Test 1 Failed",0,A.getVersion());
        }catch(AssertionFailedError e){
            System.out.println(e.getMessage());
        }

        A.inc();//by Definition Version number changed so await should throw exception t1 should return to wor
        try {
            t1.join();
        }catch(InterruptedException ignored){}

        try{
            assertEquals("testAwait Test 2 Failed",2 ,A.getVersion());
        }catch(AssertionFailedError e){
            System.out.println(e.getMessage());
        }

        Thread t2 = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    A.await(2);
                }catch(InterruptedException e){
                        try{
                            assertEquals("testAwait Test 3 Failed",3 ,A.getVersion());
                        }catch(AssertionFailedError e2){
                            System.out.println(e2.getMessage());
                        }
                }
        }});
        t2.start();
        A.inc();
    }


}
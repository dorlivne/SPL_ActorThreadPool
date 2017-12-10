package bgu.spl.a2;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

public class PromiseTest extends TestCase {

    public void testGet(){
        Promise<String> p2 = new Promise<>();
        Promise<Integer> p = new Promise<>();
        try {
            p.resolve(0);
            int x = p.get();
            assertEquals("testGet Test 1 failed ", 0, x);
        }catch(AssertionFailedError e){
            System.out.print(e.getMessage());
        }
        try {
            String answer = p2.get();//trying to get answer when we have'nt resolved p2 yet
        }catch(IllegalStateException e){
            p2.resolve("Answer");
            assertEquals("testGet Test 2 failed","Answer", p2.get());
        }
    }

    public void testIsResolved() {
        Promise<Integer> p1 = new Promise<>();
        try{
            assertEquals("IsResolved Test 1 failed",false,p1.isResolved());
        }catch(AssertionFailedError e){
            System.out.print(e.getMessage());
        }
        p1.resolve(1);
        try{
            assertEquals("IsResolved Test 2 failed",true,p1.isResolved());
        }catch(AssertionFailedError e2){
            System.out.print(e2.getMessage());
        }
    }

    public void testResolve() {
        Promise<Integer> p1 = new Promise<>();
        try{
            p1.resolve(0);
            p1.resolve(5);
        }catch(IllegalStateException e){
            if(!p1.isResolved()){
                System.out.print("Resolve Test 1 failed");
                fail();
            }
            if(p1.get() != 0) {
                System.out.print("Resolve Test 2 failed");
                fail();
            }
        }
    }

    public void testSubscribe() {
        Promise<Integer> p1 = new Promise<>();
        CallBoolean A = new CallBoolean();
        p1.subscribe(new callback() {
                         @Override
                         public void call() {
                             A.GotCalled();
                         }
                     });
        p1.resolve(515155);
        if(!A.getBool()){
            System.out.print("Subscribe Test 1  failed");
            fail();
        }
    }
}
package bgu.spl.a2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import bgu.spl.a2.sim.actions.*;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;
import java.util.Vector;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import java.util.HashMap;


class confirmation extends Action{
    String clientA;
    String clientB;
    String bankB;
    PrivateState bankState;

    public confirmation(String clientA,String clientB,String bankB,PrivateState bankState){
        this.clientA = clientA;
        this.clientB =clientB;
        this.bankB =bankB;
        this.bankState =bankState;

    }
    @Override
    protected void start() {
        this.getResult().resolve(true);//just for test lets say the other bank always approve the transaction
    }
}



class Transmission extends Action{
    int amount;
    String clientA;
    String clientB;
    String bankA;
    String bankB;
    PrivateState bankState;
    VersionMonitor vm ;
    public Transmission(int amount,String clientA,String clientB,String bankA,String bankB,PrivateState bankState){
        this.amount = amount;
        this.clientA =clientA;
        this.clientB = clientB;
        this.bankA =bankA;
        this.bankB =bankB;
        this.bankState =bankState;
        vm = new VersionMonitor();
    }

    public VersionMonitor getVm(){
        return vm;
    }
    protected void start(){
        System.out.println("Start Transmission");
        List<Action<Boolean>> actions = new ArrayList<>();
        Action<Boolean> confAction1 = new confirmation(clientA,clientB,bankB,new PrivateState() {});
        Action<Boolean> confAction = new confirmation(clientA,clientB,bankB,new PrivateState() {});
        actions.add(confAction);
        actions.add(confAction1);
        sendMessage(confAction1, bankB, new PrivateState() {});
        sendMessage(confAction, bankB, new PrivateState() {});
        then(actions,()->{
            Boolean result = actions.get(0).getResult().get();
            if(result==true){
                complete("transmission good");
                System.out.println("transmission good");
            }
            else{
                complete("transmission bad");
                System.out.println("transmission bad");
            }
        });
       /* List <Boolean> x = this.pool.getThread();
        for (boolean y:x
             ) {
            System.out.println(y);
        }*/
    }

}

public class BankTest {

    public static void main(String[] args) throws InterruptedException {
        /*ActorThreadPool pool = new ActorThreadPool(6);
        pool.start();
        Action<String> trans = new Transmission(100,"A","B","bank1","bank2",new PrivateState(){});
        Action<String> trans1 = new Transmission(100,"B","A","bank2","bank1",new PrivateState(){});
        Action<String> trans2 = new Transmission(100,"A","B","bank1","bank3",new PrivateState(){});
        Action<String> trans3 = new Transmission(100,"A","B","bank3","bank2",new PrivateState(){});
        Action<String> trans4 = new Transmission(100,"C","B","bank2","bank3",new PrivateState(){});
        Action<String> trans5 = new Transmission(100,"A","B","bank1","bank2",new PrivateState(){});
        pool.submit(trans1,"bank2",new PrivateState() {});
        pool.submit(trans,"bank1",new PrivateState() {});
    //    pool.start();
        pool.submit(trans2,"bank1",new PrivateState() {});
        pool.submit(trans3,"bank3",new PrivateState() {});
        pool.submit(trans4,"bank2",new PrivateState() {});
        pool.submit(trans5,"bank1",new PrivateState() {});

        CountDownLatch l = new CountDownLatch(6);
        trans.getResult().subscribe(()-> l.countDown());
        trans1.getResult().subscribe(()-> l.countDown());
        trans2.getResult().subscribe(()-> l.countDown());
        trans3.getResult().subscribe(()-> l.countDown());
        trans4.getResult().subscribe(()-> l.countDown());
        trans5.getResult().subscribe(()-> l.countDown());
        try {
            l.await();
        } catch (InterruptedException e) {
        }
        pool.shutdown();
    }*/

  /*      for (int i = 0; i < 300; i++) {
            ActorThreadPool pool = new ActorThreadPool(90);
            Action<Boolean> AddStudent0 = new AddStudent("CS", "Tom");
            Action<Boolean> AddStudent1 = new AddStudent("CS", "Dor");
            Action<Boolean> AddStudent2 = new AddStudent("CS", "Amit");
            Action<Boolean> AddStudent3 = new AddStudent("History", "Yossi");


            Action<Boolean> PartInCourse0 = new ParticipateInCourse("Tom", "DataStructers", 56);
            Action<Boolean> PartInCourse1 = new ParticipateInCourse("Dor", "DataStructers", 88);
            Action<Boolean> PartInCourse2 = new ParticipateInCourse("Amit", "Linear Algebra", 30);
            //     Action<Boolean> PartInCourse3 = new ParticipateInCourse("Tom","System Programing",56);

            Action<Boolean> OpenCourse0 = new OpenANewCourse("DataStructers", 10, new Vector<String>(), "CS");
            Action<Boolean> OpenCourse1 = new OpenANewCourse("Linear Algebra", 10, new Vector<String>(), "Math");
            Action<Boolean> OpenCourse3 = new OpenANewCourse("abc", 1, new Vector<String>(), "CS");
            Action<Boolean> AddSpace = new AddSpaces("abc", 10);


            Vector<String> pre = new Vector<>();
            pre.add("DataStructers");
            pre.add("abc");
            Action<Boolean> OpenCourse2 = new OpenANewCourse("System Programing", 10, pre, "CS");
            List<String> X = new LinkedList<>();
            X.add("System Programing");
            X.add("Linear Algebra");
            List<Integer> Y = new LinkedList<>();
            Y.add(56);
            Y.add(70);

            List<String> Z = new LinkedList<>();
            Z.add("Linear Algebra");
            Z.add("System Programing");
            Action<Boolean> PartInCourse6 = new ParticipateInCourse("Tom", "abc", 30);

            Action<Boolean> PartInCourse4 = new RegisterStudent("Tom", X, Y);

            Action<Boolean> PartInCourse5 = new RegisterStudent("Yossi", Z, Y);

            Action<Boolean> CloseCourse = new CloseCourse("CS", "DataStructers");

            Action<Boolean> unregister0 = new Unregister("Dor", "DataStructers");

            Action<Boolean> CloseCourse1 = new CloseCourse("Math", "Linear Algebra");


            pool.submit(AddStudent0, "Tom", new StudentPrivateState());
            pool.submit(AddStudent1, "Dor", new StudentPrivateState());
            pool.submit(AddStudent2, "Amit", new StudentPrivateState());
            pool.submit(AddStudent3, "Yossi", new StudentPrivateState());
            pool.submit(OpenCourse3, "abc", new CoursePrivateState());
            pool.submit(AddSpace, "abc", new CoursePrivateState());

            pool.start();

            pool.submit(OpenCourse0, "DataStructers", new CoursePrivateState());
            pool.submit(OpenCourse1, "Linear Algebra", new CoursePrivateState());
            pool.submit(OpenCourse2, "System Programing", new CoursePrivateState());


            // pool.submit(PartInCourse3,"Tom",new StudentPrivateState());
            pool.submit(PartInCourse0, "Tom", new StudentPrivateState());
            pool.submit(PartInCourse1, "Dor", new StudentPrivateState());
            pool.submit(PartInCourse2, "Amit", new StudentPrivateState());
            pool.submit(PartInCourse4, "Tom", new StudentPrivateState());
            pool.submit(PartInCourse5, "Yossi", new StudentPrivateState());
            pool.submit(PartInCourse6, "Tom", new StudentPrivateState());

            pool.submit(unregister0, "Dor", new StudentPrivateState());
            pool.submit(CloseCourse1, "Linear Algebra", new CoursePrivateState());
            pool.submit(CloseCourse, "DataStructers", new CoursePrivateState());


            CountDownLatch l = new CountDownLatch(16);
            OpenCourse0.getResult().subscribe(() -> l.countDown());
            OpenCourse1.getResult().subscribe(() -> l.countDown());
            OpenCourse2.getResult().subscribe(() -> l.countDown());
            OpenCourse3.getResult().subscribe(() -> l.countDown());
            AddStudent0.getResult().subscribe(() -> l.countDown());
            AddStudent1.getResult().subscribe(() -> l.countDown());
            AddStudent2.getResult().subscribe(() -> l.countDown());
            AddStudent3.getResult().subscribe(() -> l.countDown());
            AddSpace.getResult().subscribe(() -> l.countDown());
            PartInCourse0.getResult().subscribe(() -> l.countDown());
            PartInCourse1.getResult().subscribe(() -> l.countDown());
            PartInCourse2.getResult().subscribe(() -> l.countDown());
            PartInCourse4.getResult().subscribe(() -> l.countDown());
            PartInCourse5.getResult().subscribe(() -> l.countDown());
            //   PartInCourse3.getResult().subscribe(()-> l.countDown());
            PartInCourse6.getResult().subscribe(() -> l.countDown());
            CloseCourse.getResult().subscribe(() -> l.countDown());
            try {
                l.await();
            } catch (InterruptedException e) {
            }
            pool.shutdown();

            System.out.println("END OF TEST");
        }
    }
}*/


     /*   for (int i=0 ; i<1 ; i++) {
            ActorThreadPool pool = new ActorThreadPool(5);
            pool.start();

            Action<Boolean> OpenCourse0 = new OpenANewCourse("DataStructers", 15,new Vector<String>(), "CS");
            pool.submit(OpenCourse0, "DataStructers", new CoursePrivateState());

            CountDownLatch l1 = new CountDownLatch(1);
            OpenCourse0.getResult().subscribe(() -> l1.countDown());
            try {
                l1.await();
            } catch (InterruptedException e) {
            }

            Action<Boolean> AddStudent0 = new AddStudent("CS","Tom");
            Action<Boolean> AddStudent1 = new AddStudent("CS","Dor");
            Action<Boolean> AddStudent2 = new AddStudent("CS", "Amit");
            Action<Boolean> AddStudent3 = new AddStudent("CS", "Sasha");
            Action<Boolean> AddStudent4 = new AddStudent("CS", "Shir1");
            Action<Boolean> AddStudent5 = new AddStudent("CS", "Shir2");
            Action<Boolean> AddStudent6 = new AddStudent("CS", "Aviran");
            Action<Boolean> AddStudent7 = new AddStudent("CS","Biran");
            Action<Boolean> AddStudent8 = new AddStudent("CS", "Hoze");
            Action<Boolean> AddStudent9 = new AddStudent("CS", "Bibi");
            Action<Boolean> AddStudent10 = new AddStudent("CS", "Yair");
            Action<Boolean> AddStudent11 = new AddStudent("CS", "David");

            pool.submit(AddStudent0, "Tom", new StudentPrivateState());
            pool.submit(AddStudent1, "Dor", new StudentPrivateState());
            pool.submit(AddStudent2, "Amit", new StudentPrivateState());
            pool.submit(AddStudent3, "Sasha", new StudentPrivateState());
            pool.submit(AddStudent4, "Shir1", new StudentPrivateState());
            pool.submit(AddStudent5, "Shir2", new StudentPrivateState());
            pool.submit(AddStudent6, "Aviran", new StudentPrivateState());
            pool.submit(AddStudent7, "Biran", new StudentPrivateState());
            pool.submit(AddStudent8, "Hoze", new StudentPrivateState());
            pool.submit(AddStudent9, "Bibi", new StudentPrivateState());
            pool.submit(AddStudent10, "Yair", new StudentPrivateState());
            pool.submit(AddStudent11, "David", new StudentPrivateState());

            Action<Boolean> PartInCourse0 = new ParticipateInCourse("Tom","DataStructers", 10);
            Action<Boolean> PartInCourse1 = new ParticipateInCourse("Dor" , "DataStructers", 20);
            Action<Boolean> PartInCourse2 = new ParticipateInCourse("Amit","DataStructers", 30);
            Action<Boolean> PartInCourse3 = new ParticipateInCourse("Sasha","DataStructers", 40);
            Action<Boolean> PartInCourse4 = new ParticipateInCourse("Shir1","DataStructers", 50);
            Action<Boolean> PartInCourse5 = new ParticipateInCourse("Shir2","DataStructers", 60);
            Action<Boolean> PartInCourse6 = new ParticipateInCourse("Aviran","DataStructers", 70);
            Action<Boolean> PartInCourse7 = new ParticipateInCourse("Biran","DataStructers", 80);
            Action<Boolean> PartInCourse8 = new ParticipateInCourse("Hoze","DataStructers", 90);
            Action<Boolean> PartInCourse9 = new ParticipateInCourse("Bibi","DataStructers", 100);
            Action<Boolean> PartInCourse10 = new ParticipateInCourse("Yair","DataStructers", 101);
            Action<Boolean> PartInCourse11 = new ParticipateInCourse("David","DataStructers", 120);

            pool.submit(PartInCourse0, "Tom", new StudentPrivateState());
            pool.submit(PartInCourse1, "Dor", new StudentPrivateState());
            pool.submit(PartInCourse2, "Amit", new StudentPrivateState());
            pool.submit(PartInCourse3, "Sasha", new StudentPrivateState());
            pool.submit(PartInCourse4, "Shir1", new StudentPrivateState());
            pool.submit(PartInCourse5, "Shir2", new StudentPrivateState());
            pool.submit(PartInCourse6, "Aviran", new StudentPrivateState());
            pool.submit(PartInCourse7, "Biran", new StudentPrivateState());
            pool.submit(PartInCourse8, "Hoze", new StudentPrivateState());
            pool.submit(PartInCourse9, "Bibi", new StudentPrivateState());
            pool.submit(PartInCourse10, "Yair", new StudentPrivateState());
            pool.submit(PartInCourse11, "David", new StudentPrivateState());

            Action<Boolean> AddNewPlace = new AddSpaces("DataStructers", 30);

            pool.submit(AddNewPlace,"DataStructers",new CoursePrivateState());

            CountDownLatch l = new CountDownLatch(25);
            AddStudent0.getResult().subscribe(() -> l.countDown());
            AddStudent1.getResult().subscribe(() -> l.countDown());
            AddStudent2.getResult().subscribe(() -> l.countDown());
            AddStudent3.getResult().subscribe(() -> l.countDown());
            AddStudent4.getResult().subscribe(() -> l.countDown());
            AddStudent5.getResult().subscribe(() -> l.countDown());
            PartInCourse0.getResult().subscribe(() -> l.countDown());
            PartInCourse1.getResult().subscribe(() -> l.countDown());
            PartInCourse2.getResult().subscribe(() -> l.countDown());
            PartInCourse3.getResult().subscribe(() -> l.countDown());
            PartInCourse4.getResult().subscribe(() -> l.countDown());
            PartInCourse5.getResult().subscribe(() -> l.countDown());
            AddStudent6.getResult().subscribe(() -> l.countDown());
            AddStudent7.getResult().subscribe(() -> l.countDown());
            AddStudent8.getResult().subscribe(() -> l.countDown());
            AddStudent9.getResult().subscribe(() -> l.countDown());
            AddStudent10.getResult().subscribe(() -> l.countDown());
            AddStudent11.getResult().subscribe(() -> l.countDown());
            PartInCourse6.getResult().subscribe(() -> l.countDown());
            PartInCourse7.getResult().subscribe(() -> l.countDown());
            PartInCourse8.getResult().subscribe(() -> l.countDown());
            PartInCourse9.getResult().subscribe(() -> l.countDown());
            PartInCourse10.getResult().subscribe(() -> l.countDown());
            PartInCourse11.getResult().subscribe(() -> l.countDown());
            AddNewPlace.getResult().subscribe(() -> l.countDown());
            try {
                l.await();
            } catch (InterruptedException e) {
            }

            HashMap<String,Computer> computers = new HashMap<>();
            computers.put("A",new Computer("A",-1,1));
            computers.put("B",new Computer("B",-2,2));
            computers.put("C",new Computer("C",-3,3));
            computers.get("A").setSig(-1,1);
            computers.get("B").setSig(-2,2);
            computers.get("C").setSig(-3,3);

            Warehouse warehouse = new Warehouse(computers);

            LinkedList<String> Students0 = new LinkedList<>();
            Students0.add("Tom");
            Students0.add("Shir1");
            LinkedList<String> Cond0 = new LinkedList<>();
            Cond0.add("DataStructers");

            Action<Boolean> Check0 = new CheckAdministrativeObligations(Students0,Cond0,"A",warehouse);

            LinkedList<String> Students1 = new LinkedList<>();
            Students1.add("Amit");
            Students1.add("Dor");
            LinkedList<String> Cond1 = new LinkedList<>();
            Cond1.add("DataStructers");

            Action<Boolean> Check1 = new CheckAdministrativeObligations(Students1,Cond1,"A",warehouse);

            LinkedList<String> Students2 = new LinkedList<>();
            Students2.add("Sasha");
            Students2.add("Shir2");
            LinkedList<String> Cond2 = new LinkedList<>();
            Cond2.add("DataStructers");

            Action<Boolean> Check2 = new CheckAdministrativeObligations(Students2,Cond2,"A",warehouse);


            LinkedList<String> Students3 = new LinkedList<>();
            Students3.add("Aviran");
            Students3.add("Hoze");
            LinkedList<String> Cond3 = new LinkedList<>();
            Cond3.add("DataStructers");

            Action<Boolean> Check3 = new CheckAdministrativeObligations(Students3,Cond3,"A",warehouse);

            LinkedList<String> Students4 = new LinkedList<>();
            Students4.add("Bibi");
            Students4.add("Yair");
            LinkedList<String> Cond4 = new LinkedList<>();
            Cond4.add("DataStructers");

            Action<Boolean> Check4 = new CheckAdministrativeObligations(Students4,Cond4,"B",warehouse);

            LinkedList<String> Students5 = new LinkedList<>();
            Students5.add("David");
            Students5.add("Biran");
            LinkedList<String> Cond5 = new LinkedList<>();
            Cond5.add("DataStructers");

            Action<Boolean> Check5 = new CheckAdministrativeObligations(Students5,Cond5,"A",warehouse);

            pool.submit(Check0,"CS", new DepartmentPrivateState());
            pool.submit(Check4,"CS4", new DepartmentPrivateState());
            pool.submit(Check5,"CS5", new DepartmentPrivateState());

            CountDownLatch l3 = new CountDownLatch(3);
            Check0.getResult().subscribe(() -> l3.countDown());
            Check4.getResult().subscribe(() -> l3.countDown());
            Check5.getResult().subscribe(() -> l3.countDown());
            try {
                l3.await();
            } catch (InterruptedException e) {
            }

            pool.submit(Check1,"CS0", new DepartmentPrivateState());
            pool.submit(Check2,"CS1", new DepartmentPrivateState());
            pool.submit(Check3,"CS3", new DepartmentPrivateState());

            CountDownLatch l4 = new CountDownLatch(3);
            Check1.getResult().subscribe(() -> l4.countDown());
            Check2.getResult().subscribe(() -> l4.countDown());
            Check3.getResult().subscribe(() -> l4.countDown());
            try {
                l4.await();
            } catch (InterruptedException e) {
            }
            System.out.println(i);
           pool.shutdown();
        }
        System.out.println("END OF TEST");
    }
}*/

        ActorThreadPool pool = new ActorThreadPool(15);

        Vector<String> Infi2Vec = new Vector<>();
        Infi2Vec.add("Infi1");
        Infi2Vec.add("Linear Algebra");

        Vector<String> Psy2Vec = new Vector<>();
        Psy2Vec.add("Psy1");

        Vector<String> Psy3Vec = new Vector<>();
        Psy3Vec.add("Psy1");
        Psy3Vec.add("Psy2");

        Vector<String> DSVec = new Vector<>();
        DSVec.add("Intro to CS");

        Vector<String> Splvec = new Vector<>();
        Splvec.add("Intro to CS");
        Splvec.add("DS");

        Vector<String> AlgoVec = new Vector<>();
        AlgoVec.add("Intro to CS");
        AlgoVec.add("DS");
        AlgoVec.add("Spl");

        Vector<String> CPUVec = new Vector<>();
        CPUVec.add("Intro to CS");
        CPUVec.add("DS");
        CPUVec.add("Spl");
        CPUVec.add("Algo");

        Vector<String> VinikovPref = new Vector<>();
        VinikovPref.add("Algo");
        VinikovPref.add("Psy2");
        VinikovPref.add("Psy3");
        VinikovPref.add("Writing");

        Vector<Integer> VinikovGrades = new Vector<>();
        VinikovGrades.add(100);
        VinikovGrades.add(100);
        VinikovGrades.add(100);
        VinikovGrades.add(100);

        Action<Boolean> AddStudent0 = new AddStudent("CS", "Kodish");
        Action<Boolean> AddStudent1 = new AddStudent("EEE", "Shlomo");
        Action<Boolean> AddStudent2 = new AddStudent("CS", "Zipper");
        Action<Boolean> AddStudent3 = new AddStudent("Math", "Shmowitch");
        Action<Boolean> AddStudent4 = new AddStudent("Psy", "Chloriaan");
        Action<Boolean> AddStudent5 = new AddStudent("Psy", "Joung");
        Action<Boolean> AddStudent6 = new AddStudent("Math", "Vinikov");
        Action<Boolean> AddStudent7 = new AddStudent("CS", "Adler");
        Action<Boolean> AddStudent8 = new AddStudent("CS", "Hussain");
        Action<Boolean> AddStudent9 = new AddStudent("EEE", "Hannan");
        Action<Boolean> AddStudent10 = new AddStudent("EEE", "Dvora");
        Action<Boolean> AddStudent11 = new AddStudent("General", "Linda");

        Action<Boolean> OpenANewCourse0 = new OpenANewCourse("Intro to CS", 5, new Vector<>(), "CS");
        Action<Boolean> OpenANewCourse1 = new OpenANewCourse("Linear Algebra", 3, new Vector<>(), "Math");
        Action<Boolean> OpenANewCourse2 = new OpenANewCourse("Infi1", 5, new Vector<>(), "Math");
        Action<Boolean> OpenANewCourse3 = new OpenANewCourse("Infi2", 3, Infi2Vec, "Math");
        Action<Boolean> OpenANewCourse4 = new OpenANewCourse("Psy2", 3, Psy2Vec, "Psy");
        Action<Boolean> OpenANewCourse5 = new OpenANewCourse("Psy1", 3, new Vector<>(), "Psy");
        Action<Boolean> OpenANewCourse6 = new OpenANewCourse("Psy3", 3, Psy3Vec, "Psy");
        Action<Boolean> OpenANewCourse7 = new OpenANewCourse("Writing", 3, new Vector<>(), "General");
        Action<Boolean> OpenANewCourse8 = new OpenANewCourse("DS", 3, DSVec, "CS");
        Action<Boolean> OpenANewCourse9 = new OpenANewCourse("Spl", 3, Splvec, "CS");
        Action<Boolean> OpenANewCourse10 = new OpenANewCourse("Algo", 3, AlgoVec, "CS");
        Action<Boolean> OpenANewCourse11 = new OpenANewCourse("CPU", 3, CPUVec, "EEE");


        pool.submit(AddStudent0, "CS", new DepartmentPrivateState());
        pool.submit(AddStudent1, "EEE", new DepartmentPrivateState());
        pool.submit(AddStudent2, "CS", new DepartmentPrivateState());
        pool.submit(AddStudent3, "Math", new DepartmentPrivateState());
        pool.submit(AddStudent4, "Psy", new DepartmentPrivateState());
        pool.submit(AddStudent5, "Psy", new DepartmentPrivateState());
        pool.submit(AddStudent6, "Math", new DepartmentPrivateState());
        pool.submit(AddStudent7, "CS", new DepartmentPrivateState());
        pool.submit(AddStudent8, "CS", new DepartmentPrivateState());
        pool.submit(AddStudent9, "EEE", new DepartmentPrivateState());
        pool.submit(AddStudent10, "EEE", new DepartmentPrivateState());
        pool.submit(AddStudent11, "General", new DepartmentPrivateState());
        pool.submit(OpenANewCourse0, "CS", new DepartmentPrivateState());
        pool.submit(OpenANewCourse1, "Math", new DepartmentPrivateState());
        pool.submit(OpenANewCourse2, "Math", new DepartmentPrivateState());
        pool.submit(OpenANewCourse3, "Math", new DepartmentPrivateState());
        pool.submit(OpenANewCourse4, "Psy", new DepartmentPrivateState());
        pool.submit(OpenANewCourse5, "Psy", new DepartmentPrivateState());
        pool.submit(OpenANewCourse6, "Psy", new DepartmentPrivateState());
        pool.submit(OpenANewCourse7, "General", new DepartmentPrivateState());
        pool.submit(OpenANewCourse8, "CS", new DepartmentPrivateState());
        pool.submit(OpenANewCourse9, "CS", new DepartmentPrivateState());
        pool.submit(OpenANewCourse10, "CS", new DepartmentPrivateState());
        pool.submit(OpenANewCourse11, "EEE", new DepartmentPrivateState());


        pool.start();

        CountDownLatch Phase1 = new CountDownLatch(24);
        AddStudent0.getResult().subscribe(() -> Phase1.countDown());
        AddStudent1.getResult().subscribe(() -> Phase1.countDown());
        AddStudent2.getResult().subscribe(() -> Phase1.countDown());
        AddStudent3.getResult().subscribe(() -> Phase1.countDown());
        AddStudent4.getResult().subscribe(() -> Phase1.countDown());
        AddStudent5.getResult().subscribe(() -> Phase1.countDown());
        AddStudent6.getResult().subscribe(() -> Phase1.countDown());
        AddStudent7.getResult().subscribe(() -> Phase1.countDown());
        AddStudent8.getResult().subscribe(() -> Phase1.countDown());
        AddStudent9.getResult().subscribe(() -> Phase1.countDown());
        AddStudent10.getResult().subscribe(() -> Phase1.countDown());
        AddStudent11.getResult().subscribe(() -> Phase1.countDown());
        OpenANewCourse0.getResult().subscribe(() -> Phase1.countDown());
        OpenANewCourse1.getResult().subscribe(() -> Phase1.countDown());
        OpenANewCourse2.getResult().subscribe(() -> Phase1.countDown());
        OpenANewCourse3.getResult().subscribe(() -> Phase1.countDown());
        OpenANewCourse4.getResult().subscribe(() -> Phase1.countDown());
        OpenANewCourse5.getResult().subscribe(() -> Phase1.countDown());
        OpenANewCourse6.getResult().subscribe(() -> Phase1.countDown());
        OpenANewCourse7.getResult().subscribe(() -> Phase1.countDown());
        OpenANewCourse8.getResult().subscribe(() -> Phase1.countDown());
        OpenANewCourse9.getResult().subscribe(() -> Phase1.countDown());
        OpenANewCourse10.getResult().subscribe(() -> Phase1.countDown());
        OpenANewCourse11.getResult().subscribe(() -> Phase1.countDown());
        try {
            Phase1.await();
        } catch (InterruptedException e) {
        }

        System.out.println("END OF Phase1");

        Action<Boolean> PartInCourse0 = new ParticipateInCourse("Kodish", "Intro to CS", 60);
        Action<Boolean> PartInCourse1 = new ParticipateInCourse("Shlomo", "Intro to CS", 0);
        Action<Boolean> PartInCourse2 = new ParticipateInCourse("Shlomo", "Spl", 12);
        Action<Boolean> PartInCourse3 = new ParticipateInCourse("Shlomo", "CPU", 90);
        Action<Boolean> PartInCourse4 = new ParticipateInCourse("Zipper", "Intro to CS", 60);
        Action<Boolean> PartInCourse5 = new ParticipateInCourse("Zipper", "DS", 60);
        Action<Boolean> PartInCourse6 = new ParticipateInCourse("Zipper", "Spl", 60);
        Action<Boolean> PartInCourse7 = new ParticipateInCourse("Zipper", "CPU", 60);
        Action<Boolean> PartInCourse8 = new ParticipateInCourse("Hussain", "Intro to CS", 60);
        Action<Boolean> PartInCourse9 = new ParticipateInCourse("Hussain", "DS", 60);
        Action<Boolean> PartInCourse10 = new ParticipateInCourse("Hussain", "Spl", 60);
        Action<Boolean> PartInCourse11 = new ParticipateInCourse("Hussain", "Algo", 60);
        Action<Boolean> PartInCourse12 = new ParticipateInCourse("Hussain", "CPU", 60);
        Action<Boolean> PartInCourse13 = new ParticipateInCourse("Vinikov", "Intro to CS", 60);
        Action<Boolean> PartInCourse14 = new ParticipateInCourse("Vinikov", "Linear Algebra", 60);
        Action<Boolean> PartInCourse15 = new ParticipateInCourse("Vinikov", "Infi1", 60);
        Action<Boolean> PartInCourse16 = new ParticipateInCourse("Vinikov", "Infi2", 60);
        Action<Boolean> PartInCourse17 = new ParticipateInCourse("Linda", "DS", 60);
        Action<Boolean> PartInCourse18 = new ParticipateInCourse("Linda", "Writing", 60);
        Action<Boolean> PartInCourse19 = new ParticipateInCourse("Kodish", "Intro to CS", 60);
        Action<Boolean> PartInCourse20 = new ParticipateInCourse("Kodish", "DS", 60);
        Action<Boolean> PartInCourse21 = new ParticipateInCourse("Kodish", "Intro to CS", 60);
        Action<Boolean> Unregister0 = new Unregister("Vinikov", "Linear Algebra");
        Action<Boolean> PartInCourse22 = new ParticipateInCourse("Vinikov", "CPU", 60);
        Action<Boolean> Register0 = new RegisterStudent("Vinikov", VinikovPref, VinikovGrades);
        Action<Boolean> AddSpaces0 = new AddSpaces("Intro to CS", 10000000);
        Action<Boolean> CloseACourse0 = new CloseCourse("CS", "Intro to CS");
        Action<Boolean> CloseACourse1 = new CloseCourse("General", "Writing");
        Action<Boolean> Unregister1 = new Unregister("Vinikov", "Linear Algebra");
        Action<Boolean> PartInCourse23 = new ParticipateInCourse("Vinikov", "Linear Algebra", 60);



        pool.submit(PartInCourse0, "Intro to CS", new CoursePrivateState());
        pool.submit(PartInCourse1, "Intro to CS", new CoursePrivateState());
        pool.submit(PartInCourse2, "Spl", new CoursePrivateState());
        pool.submit(PartInCourse3, "CPU", new CoursePrivateState());
        pool.submit(PartInCourse4, "Intro to CS", new CoursePrivateState());
        pool.submit(PartInCourse5, "DS", new CoursePrivateState());
        pool.submit(PartInCourse6, "Spl", new CoursePrivateState());
        pool.submit(PartInCourse7, "CPU", new CoursePrivateState());
        pool.submit(PartInCourse8, "Intro to CS", new CoursePrivateState());
        pool.submit(PartInCourse9, "DS", new CoursePrivateState());
        pool.submit(PartInCourse10, "Spl", new CoursePrivateState());
        pool.submit(PartInCourse11, "Algo", new CoursePrivateState());
        pool.submit(PartInCourse12, "CPU", new CoursePrivateState());
        pool.submit(PartInCourse13, "Intro to CS", new CoursePrivateState());
        pool.submit(PartInCourse14, "Linear Algebra", new CoursePrivateState());
        pool.submit(PartInCourse15, "Infi1", new CoursePrivateState());
        pool.submit(PartInCourse16, "Infi2", new CoursePrivateState());
        pool.submit(PartInCourse17, "DS", new CoursePrivateState());
        pool.submit(PartInCourse18, "Writing", new CoursePrivateState());
        pool.submit(PartInCourse19, "Intro to CS", new CoursePrivateState());
        pool.submit(PartInCourse20, "Intro to CS", new CoursePrivateState());
        pool.submit(PartInCourse21, "Intro to CS", new CoursePrivateState());
        pool.submit(Unregister0,"Linear Algebra", new CoursePrivateState());
        pool.submit(PartInCourse22, "Linear Algebra", new CoursePrivateState());
        pool.submit(Register0, VinikovPref.get(0).toString(), new CoursePrivateState());
        pool.submit(CloseACourse0, "CS", new DepartmentPrivateState());
        pool.submit(AddSpaces0, "Intro to CS", new CoursePrivateState());
        pool.submit(CloseACourse1, "General", new DepartmentPrivateState());
        pool.submit(Unregister1,"Linear Algebra", new CoursePrivateState());
        pool.submit(PartInCourse23, "Linear Algebra", new CoursePrivateState());


        CountDownLatch Phase2 = new CountDownLatch(30);
        PartInCourse0.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse1.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse2.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse3.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse4.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse5.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse6.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse7.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse8.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse9.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse10.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse11.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse12.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse13.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse14.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse15.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse16.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse17.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse18.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse19.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse20.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse21.getResult().subscribe(() -> Phase2.countDown());
        PartInCourse22.getResult().subscribe(() -> Phase2.countDown());
        Unregister0.getResult().subscribe(()-> Phase2.countDown());
        Register0.getResult().subscribe(()-> Phase2.countDown());
        AddSpaces0.getResult().subscribe(()-> Phase2.countDown());
        CloseACourse0.getResult().subscribe(()-> Phase2.countDown());
        CloseACourse1.getResult().subscribe(()-> Phase2.countDown());
        PartInCourse23.getResult().subscribe(() -> Phase2.countDown());
        Unregister1.getResult().subscribe(()-> Phase2.countDown());

        try {
            Phase2.await();
        } catch (InterruptedException e) {
        }


        pool.shutdown();
        System.out.println("END OF Phase2");



        System.out.println("Resault Comparison");
        System.out.println("Phase1");
        System.out.println("////////////////Actors\\\\\\\\\\\\\\\\");
        System.out.println(pool.getActors().containsKey("Kodish") + " Kodish");
        System.out.println(pool.getActors().containsKey("Shlomo")+" Shlomo");
        System.out.println(pool.getActors().containsKey("Zipper") + " Zipper");
        System.out.println(pool.getActors().containsKey("Shmowitch") + " Shmowitch");
        System.out.println(pool.getActors().containsKey("Chloriaan") + " Chloriaan");
        System.out.println(pool.getActors().containsKey("Joung") +" Joung");
        System.out.println(pool.getActors().containsKey("Vinikov") + " Vinikov");
        System.out.println(pool.getActors().containsKey("Hussain") + "Hussain");
        System.out.println(pool.getActors().containsKey("Hannan") + " Hannan");
        System.out.println(pool.getActors().containsKey("Dvora") + " Dvora");
        System.out.println(pool.getActors().containsKey("Linda") + " Linda");


    }
}
/*
        Action<Boolean> PartInCourse0 = new ParticipateInCourse("Tom", "DataStructers", 10);
        Action<Boolean> PartInCourse1 = new ParticipateInCourse("Dor", "DataStructers", 20);
        Action<Boolean> PartInCourse2 = new ParticipateInCourse("Amit", "DataStructers", 30);
        Action<Boolean> PartInCourse3 = new ParticipateInCourse("Sasha", "DataStructers", 40);
        Action<Boolean> PartInCourse4 = new ParticipateInCourse("Shir1", "DataStructers", 50);
        Action<Boolean> PartInCourse5 = new ParticipateInCourse("Shir2", "DataStructers", 60);
        Action<Boolean> PartInCourse6 = new ParticipateInCourse("Aviran", "DataStructers", 70);
        Action<Boolean> PartInCourse7 = new ParticipateInCourse("Biran", "DataStructers", 80);
        Action<Boolean> PartInCourse8 = new ParticipateInCourse("Hoze", "DataStructers", 90);
        Action<Boolean> PartInCourse9 = new ParticipateInCourse("Bibi", "DataStructers", 100);
        Action<Boolean> PartInCourse10 = new ParticipateInCourse("Yair", "DataStructers", 101);
        Action<Boolean> PartInCourse11 = new ParticipateInCourse("David", "DataStructers", 120);

        pool.submit(PartInCourse0, "Tom", new StudentPrivateState());
        pool.submit(PartInCourse1, "Dor", new StudentPrivateState());
        pool.submit(PartInCourse2, "Amit", new StudentPrivateState());
        pool.submit(PartInCourse3, "Sasha", new StudentPrivateState());
        pool.submit(PartInCourse4, "Shir1", new StudentPrivateState());
        pool.submit(PartInCourse5, "Shir2", new StudentPrivateState());
        pool.submit(PartInCourse6, "Aviran", new StudentPrivateState());
        pool.submit(PartInCourse7, "Biran", new StudentPrivateState());
        pool.submit(PartInCourse8, "Hoze", new StudentPrivateState());
        pool.submit(PartInCourse9, "Bibi", new StudentPrivateState());
        pool.submit(PartInCourse10, "Yair", new StudentPrivateState());
        pool.submit(PartInCourse11, "David", new StudentPrivateState());

        Action<Boolean> AddNewPlace = new AddSpaces("DataStructers", 30);

        pool.submit(AddNewPlace, "DataStructers", new CoursePrivateState());

        CountDownLatch l = new CountDownLatch(24);
        AddStudent0.getResult().subscribe(() -> l.countDown());
        AddStudent1.getResult().subscribe(() -> l.countDown());
        AddStudent2.getResult().subscribe(() -> l.countDown());
        AddStudent3.getResult().subscribe(() -> l.countDown());
        AddStudent4.getResult().subscribe(() -> l.countDown());
        AddStudent5.getResult().subscribe(() -> l.countDown());
        PartInCourse0.getResult().subscribe(() -> l.countDown());
        PartInCourse1.getResult().subscribe(() -> l.countDown());
        PartInCourse2.getResult().subscribe(() -> l.countDown());
        PartInCourse3.getResult().subscribe(() -> l.countDown());
        PartInCourse4.getResult().subscribe(() -> l.countDown());
        PartInCourse5.getResult().subscribe(() -> l.countDown());
        AddStudent6.getResult().subscribe(() -> l.countDown());
        AddStudent7.getResult().subscribe(() -> l.countDown());
        AddStudent8.getResult().subscribe(() -> l.countDown());
        AddStudent9.getResult().subscribe(() -> l.countDown());
        AddStudent10.getResult().subscribe(() -> l.countDown());
        AddStudent11.getResult().subscribe(() -> l.countDown());
        PartInCourse6.getResult().subscribe(() -> l.countDown());
        PartInCourse7.getResult().subscribe(() -> l.countDown());
        PartInCourse8.getResult().subscribe(() -> l.countDown());
        PartInCourse9.getResult().subscribe(() -> l.countDown());
        PartInCourse10.getResult().subscribe(() -> l.countDown());
        PartInCourse11.getResult().subscribe(() -> l.countDown());
        AddNewPlace.getResult().subscribe(() -> l.countDown());
        try {
            l.await();
        } catch (InterruptedException e) {
        }

        HashMap<String, Computer> computers = new HashMap<>();
        computers.put("A", new Computer("A", -1, 1));
        computers.put("B", new Computer("B", -2, 2));
        computers.put("C", new Computer("C", -3, 3));
        computers.get("A").setSig(-1, 1);
        computers.get("B").setSig(-2, 2);
        computers.get("C").setSig(-3, 3);

        Warehouse warehouse = new Warehouse(computers);

        LinkedList<String> Students0 = new LinkedList<>();
        Students0.add("Tom");
        Students0.add("Shir1");
        LinkedList<String> Cond0 = new LinkedList<>();
        Cond0.add("DataStructers");

        Action<Boolean> Check0 = new CheckAdministrativeObligations(Students0, Cond0, "A", warehouse);

        LinkedList<String> Students1 = new LinkedList<>();
        Students1.add("Amit");
        Students1.add("Dor");
        LinkedList<String> Cond1 = new LinkedList<>();
        Cond1.add("DataStructers");

        Action<Boolean> Check1 = new CheckAdministrativeObligations(Students1, Cond1, "A", warehouse);

        LinkedList<String> Students2 = new LinkedList<>();
        Students2.add("Sasha");
        Students2.add("Shir2");
        LinkedList<String> Cond2 = new LinkedList<>();
        Cond2.add("DataStructers");

        Action<Boolean> Check2 = new CheckAdministrativeObligations(Students2, Cond2, "A", warehouse);


        LinkedList<String> Students3 = new LinkedList<>();
        Students3.add("Aviran");
        Students3.add("Hoze");
        LinkedList<String> Cond3 = new LinkedList<>();
        Cond3.add("DataStructers");

        Action<Boolean> Check3 = new CheckAdministrativeObligations(Students3, Cond3, "A", warehouse);

        LinkedList<String> Students4 = new LinkedList<>();
        Students4.add("Bibi");
        Students4.add("Yair");
        LinkedList<String> Cond4 = new LinkedList<>();
        Cond4.add("DataStructers");

        Action<Boolean> Check4 = new CheckAdministrativeObligations(Students4, Cond4, "B", warehouse);

        LinkedList<String> Students5 = new LinkedList<>();
        Students5.add("David");
        Students5.add("Biran");
        LinkedList<String> Cond5 = new LinkedList<>();
        Cond5.add("DataStructers");

        Action<Boolean> Check5 = new CheckAdministrativeObligations(Students5, Cond5, "A", warehouse);

        pool.submit(Check0, "CS", new DepartmentPrivateState());
        pool.submit(Check4, "CS4", new DepartmentPrivateState());
        pool.submit(Check5, "CS5", new DepartmentPrivateState());

        CountDownLatch l3 = new CountDownLatch(3);
        Check0.getResult().subscribe(() -> l3.countDown());
        Check4.getResult().subscribe(() -> l3.countDown());
        Check5.getResult().subscribe(() -> l3.countDown());
        try {
            l3.await();
        } catch (InterruptedException e) {
        }

        pool.submit(Check1, "CS0", new DepartmentPrivateState());
        pool.submit(Check2, "CS1", new DepartmentPrivateState());
        pool.submit(Check3, "CS3", new DepartmentPrivateState());

        CountDownLatch l4 = new CountDownLatch(3);
        Check1.getResult().subscribe(() -> l4.countDown());
        Check2.getResult().subscribe(() -> l4.countDown());
        Check3.getResult().subscribe(() -> l4.countDown());
        try {
            l4.await();
        } catch (InterruptedException e) {
        }
        pool.shutdown();
        System.out.println("END OF TEST");
    }
}*/

/**
 * Leader Election in Asyn Ring O(n^2) Algorithm
 * CS 249 Team #2 Rashmeet Khanuja, Anusha Vijay, Steven Yen
 */
import java.lang.reflect.Array;
import java.util.*;

public class Main {

    ArrayList<Processor> pList;
    Processor pA,pB,pC,pD,pE,pF;
    ArrayList<Thread> ThreadList;

    /**
     * Constructor - calls init to initialize graph.
     */
    public Main(){

        init(); //use init() for different test case.
    }

    /**
     * Initializes a ring topology by creating the
     * processors, setting their in and out channels,
     * and adding them to the pList field.
     * Uses the example ring on slide 14
     */
    public void init(){
        this.pA=new Processor(10);
        this.pB=new Processor(22);
        this.pC = new Processor(11);
        this.pD = new Processor(60);
        this.pE = new Processor(50);
        this.pF = new Processor(44);

        this.pA.setNeighbours(pB,pF);
        this.pB.setNeighbours(pC,pA);
        this.pC.setNeighbours(pD,pB);
        this.pD.setNeighbours(pE,pC);
        this.pE.setNeighbours(pF,pD);
        this.pF.setNeighbours(pA,pE);

        pList= new ArrayList<>(Arrays.asList(pA,pB,pC,pD,pE,pF));
        ThreadList=new ArrayList<>();

    }

    public static void main(String[] args) throws InterruptedException {

        Main m = new Main();

        //create and start all processor threads
        for(Processor pi : m.pList){
            Runnable ri = new Executor(pi);
            Thread ti = new Thread(ri);
            m.ThreadList.add(ti);
            ti.start();
        }

        //Sleep 1 sec for threads to complete before printing result


        for(Thread t : m.ThreadList){
            t.join();
        }

        //print info for all processor threads, showing who's elected leader
        for(Processor pi : m.pList){
            printProcInfo(pi);
        }

    }

    /**
     * Helper method for printing out info about each processor
     * @param p processor whose info we want to print.
     */
    public static void printProcInfo(Processor p){
        System.out.printf("Processor P%d ",p.getProcId());
        System.out.println("is leader? " + p.getIsLeader());
    }

}

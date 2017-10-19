/**
 * Leader Election in Asyn Ring O(n^2) Algorithm
 * CS 249 Team #2 Rashmeet Khanuja, Anusha Vijay, Steven Yen
 */
import java.util.*;

public class Main {

    ArrayList<Processor> pList;

    /**
     * Constructor - calls init to initialize graph.
     */
    public Main(){
        init();
    }

    /**
     * Initializes a ring topology by creating the
     * processors, setting their in and out channels,
     * and adding them to the pList field.
     */
    public void init(){
        pList = new ArrayList<>();

        Buffer C43 = new Buffer("C43");
        Buffer C32 = new Buffer("C32");
        Buffer C21 = new Buffer("C21");
        Buffer C10 = new Buffer("C10");
        Buffer C04 = new Buffer("C04");

        Processor P0 = new Processor(0,C10,C04);
        Processor P1 = new Processor(1,C21,C10);
        Processor P2 = new Processor(2,C32,C21);
        Processor P3 = new Processor(3,C43,C32);
        Processor P4 = new Processor(4,C04,C43);

        pList.add(P0);
        pList.add(P1);
        pList.add(P2);
        pList.add(P3);
        pList.add(P4);

    }

    public static void main(String[] args){

        Main m = new Main();

        //create and start all processor threads
        for(Processor pi : m.pList){
            Runnable ri = new Executor(pi);
            Thread ti = new Thread(ri);
            ti.start();
        }

        //Sleep 1 sec for threads to complete before printing result
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
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

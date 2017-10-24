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
        init2(); //use init() for different test case.
    }

    /**
     * Initializes a ring topology by creating the
     * processors, setting their in and out channels,
     * and adding them to the pList field.
     * Uses the example ring on slide 14
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

    /**
     * Alternate example
     * Initialize the ring shown on slide 11
     */
    public void init2(){
        pList = new ArrayList<>();

        Buffer P01toP44 = new Buffer("01to44");
        Buffer P44toP12 = new Buffer("44to12");
        Buffer P12toP10 = new Buffer("12to10");
        Buffer P10toP50 = new Buffer("10to50");
        Buffer P50toP33 = new Buffer("50to33");
        Buffer P33toP01 = new Buffer("33to01");

        Processor P01 = new Processor(1,P33toP01,P01toP44);
        Processor P44 = new Processor(44,P01toP44,P44toP12);
        Processor P12 = new Processor(12,P44toP12,P12toP10);
        Processor P10 = new Processor(10,P12toP10,P10toP50);
        Processor P50 = new Processor(50,P10toP50,P50toP33);
        Processor P33 = new Processor(33,P50toP33,P33toP01);

        pList.add(P01);
        pList.add(P44);
        pList.add(P12);
        pList.add(P10);
        pList.add(P50);
        pList.add(P33);

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

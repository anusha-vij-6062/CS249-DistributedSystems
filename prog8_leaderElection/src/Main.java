/**
 * Leader Election in Asyn Ring O(n^2) Algorithm
 * CS 249 Team #2 Rashmeet Khanuja, Anusha Vijay, Steven Yen
 */
import java.lang.reflect.Array;
import java.util.*;

public class Main {

    ArrayList<Processor> pList;
    Processor pA,pB,pC,pD,pE,pF;
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

    }

    public void init2(){
        Processor p10 = new Processor(10);
        Processor p22 = new Processor(22);
        Processor p11 = new Processor(11);
        Processor p44 = new Processor(44);

        p10.setNeighbours(p22,p44);
        p22.setNeighbours(p11,p10);
        p11.setNeighbours(p44,p22);
        p44.setNeighbours(p10,p11);

        pList = new ArrayList<>();
        pList.add(p10);
        pList.add(p22);
        pList.add(p11);
        pList.add(p44);
    }



    public static void main(String[] args) throws InterruptedException {

        Main m = new Main();

        for(Processor proc: m.pList){
            Message startMessage = new Message(MessageType.PROBE, proc.getProcId(), 0, 1);
            proc.getRightProcessor().sendMessageToMyBuffer(startMessage, proc);
            proc.getLeftProcessor().sendMessageToMyBuffer(startMessage, proc);
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

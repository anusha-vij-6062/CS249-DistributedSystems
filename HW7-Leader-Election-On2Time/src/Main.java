/**
 * Leader Election in Asyn Ring O(n^2) Algorithm
 * CS 249 Team #2 Rashmeet Khanuja, Anusha Vijay, Steven Yen
 */

public class Main {

    public static void main(String[] args){

        Buffer C14 = new Buffer("C14");
        Buffer C43 = new Buffer("C43");
        Buffer C32 = new Buffer("C32");
        Buffer C21 = new Buffer("C21");

        Processor P1 = new Processor(1,C21,C14);
        Processor P2 = new Processor(2,C32,C21);
        Processor P3 = new Processor(3,C43,C32);
        Processor P4 = new Processor(4,C14,C43);

        Runnable r1 = new Executor(P1);
        Runnable r2 = new Executor(P2);
        Runnable r3 = new Executor(P3);
        Runnable r4 = new Executor(P4);

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        Thread t3 = new Thread(r3);
        Thread t4 = new Thread(r4);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        //Sleep 1 sec for threads to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Check who is leader
        printProcInfo(P1);
        printProcInfo(P2);
        printProcInfo(P3);
        printProcInfo(P4);

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

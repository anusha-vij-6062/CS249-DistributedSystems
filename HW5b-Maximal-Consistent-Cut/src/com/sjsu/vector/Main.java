package com.sjsu.vector;
/**
 * CS249 Maximal Consistent Cut program
 * Class we added in addition to classes provided in skeleton
 * Class added by us in additional to the ones
 * provided in the skeleton code.
 * @author Rashmeet Khanuja, Anusha Vijay, Steven Yen
 *
 */
public class Main {
    Algorithm algo;
    int [] maximumCut;

    /**
     * Calls init() to initailize the algorithm object and the an empty maximum cut array
     **/

    Main() throws InterruptedException {
        init();
    }

    public void init() throws InterruptedException {
        this.algo = new Algorithm();
        this.maximumCut=new int[2];
    }

    /**
     * Creates a new object Runnable of class Executor (implements Runnable).
     * Runnable objects are then passed to the constructor of Thread before the threads
     * are fired.
     */

    void fireExecution(){
        Runnable r0 = new Executor(algo, algo.p0);
        Runnable r1 = new Executor(algo, algo.p1);

        Thread t0 = new Thread(r0);
        Thread t1 = new Thread(r1);

        t0.start();
        t1.start();
    }

    /**
     * Sends CUT message to the processes.
     * @param k Input cut to be sent to the processors
     * @param algo To store maximum cut which is it's object variable
     * @throws InterruptedException
     */

    void sendCut(int[] k,Algorithm algo) throws InterruptedException {
        Message cut=new Message(MessageType.CUT,k);
        algo.p0.sendMessageToMyBuffer(cut,algo.p0);
        this.maximumCut[0]=algo.p0.getMaxCut();
        algo.p1.sendMessageToMyBuffer(cut,algo.p1);
        this.maximumCut[1]=algo.p1.getMaxCut();
    }

    public static void main(String[] args) throws InterruptedException {
        Main m=new Main();
        int[] cut = {2,6};
        m.fireExecution();
        Thread.sleep(2550);
        m.sendCut(cut,m.algo);
        Thread.sleep(3000);
        System.out.println("\n--------------------Maximum Consistant Cut-------------------------------");
        System.out.printf("Based on an input cut of <%d, %d>,%n",cut[0],cut[1]);
        System.out.printf("The Final Maximal Consistent Cut is <%d, %d>",m.maximumCut[0],m.maximumCut[1]);
        System.out.println("\n-------------------------------------------------------------------------");

    }
}
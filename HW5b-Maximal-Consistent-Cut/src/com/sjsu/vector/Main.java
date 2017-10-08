package com.sjsu.vector;

/**
 * Class added by us in additional to the ones
 * provided in the skeleton code.
 * @author Rashmeet Khanuja, Anusha Vijay, Steven Yen
 *
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        init();
    }

    public static void init() throws InterruptedException {
        Algorithm algo = new Algorithm();

        Runnable r0 = new Executor(algo, algo.p0);
        Runnable r1 = new Executor(algo, algo.p1);

        Thread t0 = new Thread(r0);
        Thread t1 = new Thread(r1);

        t0.start();
        t1.start();

        //cut is = 2,6
        int[] cut = {2,6};
        Thread.sleep(5000);
        int a=algo.p0.calculateMaximalCut(cut);
        Thread.sleep(1000);
        int b=algo.p1.calculateMaximalCut(cut);
        System.out.printf("Based on an input cut of <%d, %d>,%n",cut[0],cut[1]);
        System.out.printf("The Final Maximal Consistent Cut is <%d, %d>",a,b);

    }

}
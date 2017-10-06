package com.sjsu.vector;

/**
 * Class added by us in additional to the ones
 * provided in the skeleton code.
 * @author Rashmeet Khanuja, Anusha Vijay, Steven Yen
 *
 */
public class Main {

    public static void main(String[] args){

        init();
    }

    public static void init(){
        Algorithm algo = new Algorithm();

        Runnable r0 = new Executor(algo, algo.p0);
        Runnable r1 = new Executor(algo, algo.p1);
        //Runnable r2 = new Executor(algo, algo.p2);

        Thread t0 = new Thread(r0);
        Thread t1 = new Thread(r1);
        //Thread t2 = new Thread(r2);

        t0.start();
        t1.start();
        //t2.start();
    }

}
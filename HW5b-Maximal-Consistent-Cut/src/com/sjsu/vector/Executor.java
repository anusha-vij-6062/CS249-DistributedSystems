package com.sjsu.vector;
/**
 * CS249 Maximal Consistent Cut program
 * Class we added in addition to classes provided in skeleton
 * @author Rashmeet Khanuja, Anusha Vijay, Steven Yen
 *
 */
public class Executor implements Runnable{

    Processor proc;
    Algorithm algo;

    public Executor(Algorithm algo, Processor proc){
        this.proc = proc;
        this.algo = algo;
    }

    public void run(){
        if(proc.getId()==0){
            try {
                algo.executionPlanForP0();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else if(proc.getId()==1){
            try {
                algo.executionPlanForP1();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
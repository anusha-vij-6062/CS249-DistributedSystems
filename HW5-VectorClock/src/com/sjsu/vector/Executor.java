package com.sjsu.vector;
/**
 * CS249 VectorClock program
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
			algo.executionPlanForP0();
		}else if(proc.getId()==1){
			algo.executionPlanForP1();
		}else{
			algo.executionPlanForP2();
		}
	}
	
}
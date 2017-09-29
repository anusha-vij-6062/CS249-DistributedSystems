package com.sjsu.vector;

import java.util.List;
/**
 * CS249 VectorClock Program
 * A skeleton code was provided on which we built-upon
 * @author Rashmeet Khanuja, Anusha Vijay, Steven Yen
 *
 */
public class Algorithm{
	int noOfProcessors;
	Processor p0, p1, p2; // Assume there are three processors.

	public Algorithm() {
		super();
		this.noOfProcessors = 3;
		// TODO : initialize all the processors
		p0 = new Processor(0, 3);
		p1= new Processor(1, 3);
		p2 = new Processor(2, 3);
	}

	// Write hard coded execution plan for processors
	public void executionPlanForP0() {
		// TODO: call events on P0 for compute.
		// Call send events to send message
		// Call receive messages
		compute(p0, new Message(MessageType.COMPUTATION,new VectorClock(3))); //phi00
		compute(p0, new Message(MessageType.COMPUTATION,new VectorClock(3))); //phi01
		send(p1, p0, new Message(MessageType.RECIEVE,new VectorClock(3))); //phi02
		compute(p0, new Message(MessageType.COMPUTATION,new VectorClock(3))); //phi03 
		
	}

	// Write hard coded execution plan for processors
	public void executionPlanForP1() {
		// TODO: call events on P0 for compute.
		// Call send events to send message
		// Call receive messages
		
		compute(p1, new Message(MessageType.COMPUTATION,new VectorClock(3))); //phi10
		compute(p1, new Message(MessageType.COMPUTATION,new VectorClock(3))); //phi11 
		
		receive(p1, p0); //phi12
		System.out.println("!!!P1 receieved message from p0!!!");
		
		compute(p1, new Message(MessageType.COMPUTATION,new VectorClock(3))); //phi13
		
	}

	// Write hard coded execution plan for processors
	public void executionPlanForP2() {
		// TODO: call events on P0 for compute.
		// Call send events to send message
		// Call receive messages
	}
	
	/**
	 * 
	 * @param p
	 * @param computeMessage
	 */
	public void compute(Processor p, Message computeMessage) {
		// TODO: implement. What will be the value of vector clock passed to
		// this message?
		//p.sendMessageToMyBuffer(computeMessage);
		
		p.sendMessageToMyBuffer(computeMessage,p);
	}

	public void send(Processor to, Processor from, Message m) {
		// TODO: implement. What will be the value of vector clock passed to
		// this message?
		
		to.sendMessageToMyBuffer(m,from);

	}
	
	synchronized public void receive(Processor to, Processor from){
			
		while(!((to.messageBuffer.getMessage()!=null) && (to.messageBuffer.getMessage().messageType.equals(MessageType.RECIEVE))));
	
	}
			 
}


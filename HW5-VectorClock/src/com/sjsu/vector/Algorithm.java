package com.sjsu.vector;

import static com.sjsu.vector.MessageType.RECIEVE;
import static com.sjsu.vector.MessageType.SEND;


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
    public void executionPlanForP0() throws InterruptedException {
        //compute(p0, new Message(MessageType.COMPUTATION,p0.vc.clone()));
        //compute(p0, new Message(MessageType.COMPUTATION,p0.vc.clone()));
        send(p1, p0, new Message(SEND,p0.vc.clone()));
        //compute(p0, new Message(MessageType.COMPUTATION,p0.vc.clone())); //phi03
        
        synchronized(this){
        	System.out.println("\n------The final VECTOR CLOCK for process 0 is---");
        	p0.vc.printVC();
        }

    }

    // Write hard coded execution plan for processors
    public void executionPlanForP1() throws InterruptedException {
        // TODO: call events on P0 for compute.
        compute(p1, new Message(MessageType.COMPUTATION,p1.vc.clone()));//phi11
        compute(p1, new Message(MessageType.COMPUTATION,p1.vc.clone()));
        compute(p1, new Message(MessageType.COMPUTATION,p1.vc.clone()));
        receive(p1, p0); //phi12
        //compute(p1, new Message(MessageType.COMPUTATION,p1.vc.clone()));
        
        synchronized(this){
        	System.out.println("\n----------The final VECTOR CLOCK for process 1 is----");
        	p1.vc.printVC();
        }
    }
    
    public void executionPlanForP2() throws InterruptedException{
    	compute(p2, new Message(MessageType.COMPUTATION, p2.vc.clone()));
    	compute(p2, new Message(MessageType.COMPUTATION, p2.vc.clone()));
    	compute(p2, new Message(MessageType.COMPUTATION, p2.vc.clone()));
    	compute(p2, new Message(MessageType.COMPUTATION, p2.vc.clone()));
    	
    	synchronized(this){
        	System.out.println("\n----------The final VECTOR CLOCK for process 2 is----");
        	p2.vc.printVC();
        }
    }

    private void compute(Processor p, Message computeMessage) throws InterruptedException {
        p.sendMessageToMyBuffer(computeMessage,p);
    }

    synchronized private void send(Processor to, Processor from, Message m) throws InterruptedException {
            from.sendMessageToMyBuffer(m,from);
            Message recieveMessage = new Message(RECIEVE,from.vc.clone());
            to.sendMessageToMyBuffer(recieveMessage,from);
            this.notify();
        }
    
    synchronized private void receive(Processor receiver, Processor sender){
    		if(receiver.savedProcess != null && receiver.savedProcess==sender){
    			try {
    				//receiver.calculateVectorClocks(observable, arg, recievedMessage);
					receiver.sendMessageToMyBuffer(new Message(RECIEVE,receiver.testVC.clone()), sender);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    
//    synchronized private void receive(Processor reciever,Processor sender) throws InterruptedException {
//        if(reciever.messageBuffer.getMessage()==null || reciever.messageBuffer.getMessage().messageType!=RECIEVE)
//        {
//            if(reciever.sender!=sender) {
//                this.wait();
//            }
//        }
//
//    }

}

/*
Buffer b = to.messageBuffer;
        //
        if(to.savedProcess!=null && to.savedProcess.getId()==from.getId()){
            System.out.printf("Need to consume the message");
        }
        synchronized (b) {
            //if(b.getMessage().messageType!=RECIEVE || !b.hasChanged()){
            if (!b.hasChanged()) {

                System.out.printf("\n waiting to recieve from" + from.getId());
                //from.messageBuffer.wait();
            }
        }
 */
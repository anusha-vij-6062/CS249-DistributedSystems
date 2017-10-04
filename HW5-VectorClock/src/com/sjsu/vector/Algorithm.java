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
        p2 = new Processor(2,3);
    }

    // Write hard coded execution plan for processors
    public void executionPlanForP0() throws InterruptedException {
        //compute(p0, new Message(MessageType.COMPUTATION,p0.vc.clone()));
        //compute(p0, new Message(MessageType.COMPUTATION,p0.vc.clone()));
        //send(p1, p0, new Message(SEND,p0.vc.clone()));
        //compute(p0, new Message(MessageType.COMPUTATION,p0.vc.clone())); //phi03
    	
    	//sample plan from slides
    	
    	Thread.sleep(200);
    	this.send(p1, p0, new Message(MessageType.SEND, p0.vc.clone()),200);
    	
    	Thread.sleep(500);
    	this.send(p2, p0, new Message(MessageType.SEND, p0.vc.clone()),0); //made this smaller
    	
    	Thread.sleep(400);
    	this.compute(p0, new Message(MessageType.COMPUTATION,p0.vc.clone()));
    	
    	Thread.sleep(300);
    	this.receive(p0, p1);
    	
    	Thread.sleep(400);
    	this.compute(p0, new Message(MessageType.COMPUTATION,p0.vc.clone()));
    	
    	Thread.sleep(400);
        synchronized(this){
        	System.out.println("\n------The final VECTOR CLOCK for process 0 is---");
        	p0.vc.printVC();
        }

    }

    // Write hard coded execution plan for processors
    public void executionPlanForP1() throws InterruptedException {
        // TODO: call events on P0 for compute.
        //compute(p1, new Message(MessageType.COMPUTATION,p1.vc.clone()));//phi11
        //compute(p1, new Message(MessageType.COMPUTATION,p1.vc.clone()));
        //compute(p1, new Message(MessageType.COMPUTATION,p1.vc.clone()));
        //receive(p1, p0); //phi12
        
    	//sample plan from slides
    	Thread.sleep(400);
    	this.receive(p1, p0);
    	
    	Thread.sleep(200);
    	this.receive(p1, p2); //phi7
    	
    	Thread.sleep(300); //phi8
    	this.send(p2, p1, new Message(MessageType.SEND, p1.vc.clone()),200);
    	
    	Thread.sleep(300); //phi9 (p1 recieve from p2, then sends m to p1)
    	this.recieveAndSend(p0, p1, new Message(MessageType.SEND, p1.vc.clone()));
    	//this.receive(p1, p2);
    	//this.send(p0, p1, new Message(MessageType.SEND, p1.vc.clone()));
    	
    	Thread.sleep(600);
        synchronized(this){
        	System.out.println("\n----------The final VECTOR CLOCK for process 1 is----");
        	p1.vc.printVC();
        }
    }
    
    public void executionPlanForP2() throws InterruptedException{
    	//compute(p2, new Message(MessageType.COMPUTATION,p2.vc.clone()));
    	//compute(p2, new Message(MessageType.COMPUTATION,p2.vc.clone()));
    	//compute(p2, new Message(MessageType.COMPUTATION,p2.vc.clone()));
    	//compute(p2, new Message(MessageType.COMPUTATION,p2.vc.clone()));
    	
    	Thread.sleep(100);
    	this.compute(p2, new Message(MessageType.COMPUTATION,p2.vc.clone()));
    	
    	Thread.sleep(200); //phi11
    	this.compute(p2, new Message(MessageType.COMPUTATION,p2.vc.clone()));
    	
    	Thread.sleep(200); //phi12
    	this.send(p1, p2, new Message(MessageType.SEND,p2.vc.clone()),100);
    	
    	Thread.sleep(300); //phi13
    	this.receive(p2, p0);
    	
    	Thread.sleep(100); //phi14
    	this.send(p1, p2, new Message(MessageType.SEND,p2.vc.clone()),400);
    	
    	Thread.sleep(300); //phi15
    	this.receive(p2, p1);
    	
    	Thread.sleep(300); //phi16
    	this.compute(p2, new Message(MessageType.COMPUTATION,p2.vc.clone()));
    	
    	Thread.sleep(600);
    	synchronized(this){
        	System.out.println("\n----------The final VECTOR CLOCK for process 2 is----");
        	p2.vc.printVC();
        }
    }
    
    
    private void compute(Processor p, Message computeMessage) throws InterruptedException {
        p.sendMessageToMyBuffer(computeMessage,p);
    }

    /**
     * @param to
     * @param from
     * @param m m's type is expected to be SEND
     * @throws InterruptedException
     */
    synchronized private void send(Processor to, Processor from, Message m) throws InterruptedException {
            
    		//from.sendMessageToMyBuffer(m,from); //m's type is expected to be SEND
            
    		from.calculateVectorClocks(null, null, new Message(MessageType.SEND, from.vc.clone()));
    		Message recieveMessage = new Message(RECIEVE,from.vc.clone());
    		Thread.sleep(300);
            to.sendMessageToMyBuffer(recieveMessage,from);
         
        }
    
    /**
     * Overloaded version of send with variable delay
     * @param to
     * @param from
     * @param m
     * @param milliseconnd to wait before setting message
     * @throws InterruptedException
     */
    synchronized private void send(Processor to, Processor from, Message m, int delay) throws InterruptedException {
        
		//from.sendMessageToMyBuffer(m,from); //m's type is expected to be SEND
        
		from.calculateVectorClocks(null, null, new Message(MessageType.SEND, from.vc.clone()));
		Message recieveMessage = new Message(RECIEVE,from.vc.clone());
		Thread.sleep(delay);
        to.sendMessageToMyBuffer(recieveMessage,from);
     
    }
    
    
    synchronized private void recieveAndSend(Processor sendTo, Processor sendFrom, Message m) throws InterruptedException{
    	Message recieveMessage = new Message(RECIEVE,sendFrom.vc.clone());
		Thread.sleep(100);
        sendTo.sendMessageToMyBuffer(recieveMessage,sendFrom);
    }
    
    synchronized private void receive(Processor receiver, Processor sender){
    		//if(receiver.savedProcess != null && receiver.savedProcess.getId()==sender.getId()){
    			/*
    			try {
    				//receiver.calculateVectorClocks(observable, arg, recievedMessage);
					//receiver.sendMessageToMyBuffer(new Message(RECIEVE,receiver.testVC.clone()), sender);
    				
    				//added this 100317
    				synchronized(receiver.messageBuffer){
    					//!((to.messageBuffer.getMessage()!=null) && (to.messageBuffer.getMessage().messageType.equals(MessageType.RECIEVE)))
    					while(!((receiver.messageBuffer.getMessage()!=null) && (receiver.messageBuffer.getMessage().messageType.equals(MessageType.RECIEVE)))){
    						receiver.messageBuffer.wait();
    					}
    					
    					receiver.messageBuffer.resetMessage(); //set message back to null so next wait won't be confused.
    				}
    				
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
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
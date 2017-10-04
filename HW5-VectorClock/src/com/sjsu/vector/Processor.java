package com.sjsu.vector;
import java.util.Observable;
import java.util.Observer;

/**
 * CS249 VectorClock Program
 * A skeleton code was provided on which we built-upon
 * Performs all the processor related tasks
 * @author Rashmeet Khanuja, Anusha Vijay, Steven Yen
 * @version 1.0
 *
 */
public class Processor implements Observer {
    Buffer messageBuffer ;
    Integer id ;
    VectorClock vc ; //This is the current vector clock
    Processor sender;
    Processor savedProcess; //the sender process
    VectorClock testVC;

    /**
     * Initializes the processor with id, children and unexplored lists. Adds himself in the observers list.
     * @param id of the processor
     */
    public Processor(int id, int totalProcesors) {
        messageBuffer = new Buffer();
        this.id = id;
        messageBuffer.addObserver(this);
        vc=new VectorClock(totalProcesors);
    }

    /**
     * Getter for the id of this process
     * @return process id as integer
     */
    public Integer getId(){
        return id;
    }
    /**
     * Overlaoding of above method we added that has sender param
     * @param message
     * @param sender the processor that send message to my (this processor's) buffer
     */
    public void sendMessageToMyBuffer(Message message, Processor sender) throws InterruptedException {
        System.out.printf("P%d send %s message to P%d %n",sender.id, message.messageType, this.id);
        this.messageBuffer.setMessage(message,sender);
    }

    /**
     * Gets called when a node receives a message in its buffer
     * Processes the message received in the buffer
     */
    synchronized public void update(Observable observable, Object arg) {
        sender = (Processor) arg;
        try {
            Message text = messageBuffer.getMessage();
            if(text.messageType==MessageType.SEND){
            	savedProcess = sender;
            	testVC = text.vc.clone();
            }
            
            //added
            synchronized(this.messageBuffer){
            	if(this.messageBuffer.getMessage().messageType==MessageType.RECIEVE){
            		this.messageBuffer.notify();
            	}
            }
            
            //System.out.printf(" Update: P%d recieved message from P%d %n", this.id, sender.id);
            
            //System.out.printf("\nUpdate: Message:"+text.messageType+" by "+ this.id+" From:"+sender.id);
            //System.out.println("----Received clock:---");
            //text.vc.printVC();
            
           
            calculateVectorClocks(observable, arg,text);
            
            //this.messageBuffer.resetMessage(); //reset the message buffer to null after message processed.
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void printVC(VectorClock vc){
        for (int i=0;i<vc.vc.length;i++){
            System.out.print(this.vc.vc[i]);
            System.out.printf("\t");
        }
    }

    synchronized public void calculateVectorClocks(Observable observable, Object arg,Message recievedMessage) throws InterruptedException {
        //System.out.println("Current Process "+this.id+"Clock is:");
        //this.vc.printVC();
        //System.out.println("\nUpdating its on vector clock by 1 of process id:"+this.getId());
    	// Thread.sleep(50);
    	this.vc.incrementAt(id);
        
      //this.vc.compareTo(recievedMessage.vc);
        if(recievedMessage.messageType==MessageType.RECIEVE){
	        for(int i=0; i< this.vc.vc.length; i++){
	        	if(i != this.getId()){
	        		if(this.vc.vc[i]<recievedMessage.vc.vc[i]){
	        			this.vc.updateAt(i,recievedMessage.vc.vc[i]);
	        		}
	        	}
	        }
        }
        
        //System.out.println("\nUpdated Vector of Processor "+this.id+" clock is for Message Type"+this.messageBuffer.getMessage().messageType);
        System.out.printf("  VC of P%d updated to: ", this.getId());
        this.vc.printVC();
    }
}

/*
        try {
            System.out.println("\nMessage"+this.messageBuffer.getMessage().messageType);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            if(senderP==this.sender){
            this.vc.vc = calculateVectorClocks(observable, arg);}
            else{
                System.out.printf("Sender didnt match with set value");
                //saving this buffer::
                savedProcess = senderP;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
 */
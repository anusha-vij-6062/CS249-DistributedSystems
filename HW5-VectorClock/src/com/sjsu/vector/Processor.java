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
	//TODo : add appropriate visibility indicators to each member variable
    Buffer messageBuffer ;
    Integer id ;
    //TODO: Think through when would you need a list of vector clocks
    VectorClock vc ; //This is the current vector clock 
    
    /**
     * Initializes the processor with id, children and unexplored lists. Adds himself in the observers list.
     * @param id of the processor
     */
    public Processor(int id, int totalProcesors) {
        messageBuffer = new Buffer();
        this.id = id; 
        messageBuffer.addObserver(this);
    }
    
    /**
     * Getter for the id of this process
     * @return process id as integer
     */
    public Integer getId(){
    	return id;
    }
        
    /**
     * Overloaded method, called with single argument
     * This method will add a message to this processors buffer.
     * Other processors will invoke this method to send a message to this Processor
     * @param message Message to be sent
     */
    public void sendMessageToMyBuffer(Message message){
    	//TODO: implement 
    }
    
    /**
     * Overlaoding of above method we added that has sender param
     * @param message
     * @param sender the processor that send message to my (this processor's) buffer
     */
    public void sendMessageToMyBuffer(Message message, Processor sender){
    	
    	System.out.printf("P%d send %s message to P%d %n",sender.id, message.messageType, this.id);
    	this.messageBuffer.setMessage(message,sender);
    }
    
    /**
     * Gets called when a node receives a message in its buffer
     * Processes the message received in the buffer
     */
    public void update(Observable observable, Object arg) {
    	
    	Processor senderP = (Processor) arg;
    	//System.out.printf(" Update: P%d recieved message from P%d %n", this.id, senderP.id);
    	
    	calculateVectorClocks(observable, arg);
    }

    //TODO: Discuss does this method need to return a vector clock? or is void enough.
    public void calculateVectorClocks(Observable observable, Object arg) {
    	//TODO: Implement the logic to check based on the current vector clocks and the vector clock
    	//Hint: Get vector clocks for this processor and message from this processors buffer
    	//invoke methods of VectorClock
    	
    	//Processor senderP = (Processor) arg;
    	
    	//System.out.println("  calculate vector clocks");
    	
    }
 
}

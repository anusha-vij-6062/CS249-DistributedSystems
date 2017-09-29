package com.sjsu.vector;

import java.util.Observable;

/**
 * CS249 VectorClock Program
 * Observable Buffer of each node
 * Skeleton code was provided on which we built upon
 * @author Rashmeet Khanuja, Anusha Vijay, Steven Yen
 * @version 1.0
 */
public class Buffer extends Observable {
    private Message message;

    /**
     * 
     * Creates empty buffer
     */
    public Buffer(){
    	this.message = null;
    }

    /**
     * Creates buffer with message
     * @param message Message to be stored
     */
    public Buffer(Message message) {
        this.message = message;
    }
    
    /**
     * @return Message from the buffer
     */
    public Message  getMessage() {
        return message;
    }
    
    /**
     * Sets the message and notifies the observers with the sender node's information
     * @param message		Message to be stored in the buffer
     * @param fromProcessor Node who sent the message
     */
    public void setMessage(Message message) {
        this.message = message;
        setChanged();
        notifyObservers();
    }
    
    /**
     * Overloading of original setMessage() with additional param
     * that we added, to pass the sender process to notifyObservers()
     * @param message
     * @param sender
     */
    public void setMessage(Message message, Processor sender){
    	this.message = message;
    	setChanged();
    	notifyObservers(sender);
    }
     
}


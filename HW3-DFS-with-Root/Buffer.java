//package edu.dt;

import java.util.Observable;

/**
 * Created by tphadke on 8/29/17.
 */
class Buffer extends Observable implements IBuffer {
    
	private Message message;

	/**
	* Getter for the "message" field of the class.
	* @ return the message field.
	*/
    public Message getMessage() {
        return message;
    }

	/**
	* Sets the message of the buffer (communication channel)
	* Then sets the changed flag to true. Then nofifies all observers 
	* that are attached to this Buffer so that their update methods are called.
	* @param message the message to be send to all observers attached to this buffer.
	* @param senderProcess the process that is sending the message.
	*/
    public void setMessage(Message message,IProcessor senderProcess) {
        this.message = message;
        setChanged();
		
		//the argument sendProcess is passed in to the update(Observable o, Object arg) metehod as the arg param (2nd param).
        notifyObservers(senderProcess);
    }

	/**
	* Adds an observer to the list of observers of the buffer
	* In our case only one observer (the associated process) will
	* be added as an observer of the buffer.
	* @param process to be added as an observer to the current buffer.
	*/
    public void addObserverToMyBuffer(IProcessor caller)
    {
        addObserver((Processor)caller);

    }

}


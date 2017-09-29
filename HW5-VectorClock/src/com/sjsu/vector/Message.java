package com.sjsu.vector;

/**
 * CS249 VectorClock Program 
 * Skeleton code was provided on which we built upon
 * @author Rashmeet Khanuja, Anusha Vijay, Steven Yen
 *
 */
public class Message {
	MessageType messageType;
	VectorClock vc;
	
	
	public Message(MessageType mt, VectorClock vc) {
		this.messageType=mt;
		this.vc = vc;
	}
	
}
